package ir.mrmabrosia.atlas.data

import ir.mrmabrosia.atlas.domain.Book
import ir.mrmabrosia.atlas.domain.BookRepository
import java.sql.ResultSet

class SqliteBookRepository(private val database: AtlasDatabase) : BookRepository {
    override fun all(): List<Book> = query("SELECT * FROM books ORDER BY added_at DESC")

    override fun search(query: String): List<Book> {
        if (query.isBlank()) return all()
        val pattern = "%${query.trim()}%"
        database.connection().use { connection ->
            connection.prepareStatement(
                "SELECT * FROM books WHERE title LIKE ? OR author LIKE ? OR format LIKE ? ORDER BY title COLLATE NOCASE"
            ).use { statement ->
                statement.setString(1, pattern)
                statement.setString(2, pattern)
                statement.setString(3, pattern)
                statement.executeQuery().use { result -> return result.toBooks() }
            }
        }
    }

    override fun insert(book: Book) {
        database.connection().use { connection ->
            connection.prepareStatement(
                """INSERT OR IGNORE INTO books
                   (id, title, author, format, relative_path, added_at, last_opened_at)
                   VALUES (?, ?, ?, ?, ?, ?, ?)"""
            ).use { statement ->
                statement.setString(1, book.id)
                statement.setString(2, book.title)
                statement.setString(3, book.author)
                statement.setString(4, book.format)
                statement.setString(5, book.relativePath)
                statement.setLong(6, book.addedAt)
                if (book.lastOpenedAt == null) statement.setNull(7, java.sql.Types.BIGINT)
                else statement.setLong(7, book.lastOpenedAt)
                statement.executeUpdate()
            }
        }
    }

    override fun markOpened(id: String) {
        database.connection().use { connection ->
            connection.prepareStatement("UPDATE books SET last_opened_at = ? WHERE id = ?").use { statement ->
                statement.setLong(1, System.currentTimeMillis())
                statement.setString(2, id)
                statement.executeUpdate()
            }
        }
    }

    override fun recentlyOpened(limit: Int): List<Book> = database.connection().use { connection ->
        connection.prepareStatement(
            "SELECT * FROM books WHERE last_opened_at IS NOT NULL ORDER BY last_opened_at DESC LIMIT ?"
        ).use { statement ->
            statement.setInt(1, limit.coerceAtLeast(1))
            statement.executeQuery().use { it.toBooks() }
        }
    }

    override fun count(): Int = database.connection().use { connection ->
        connection.createStatement().use { statement ->
            statement.executeQuery("SELECT COUNT(*) FROM books").use { result ->
                if (result.next()) result.getInt(1) else 0
            }
        }
    }

    private fun query(sql: String): List<Book> = database.connection().use { connection ->
        connection.createStatement().use { statement ->
            statement.executeQuery(sql).use { it.toBooks() }
        }
    }

    private fun ResultSet.toBooks(): List<Book> = buildList {
        while (next()) {
            val lastOpened = getLong("last_opened_at").let { if (wasNull()) null else it }
            add(Book(
                id = getString("id"),
                title = getString("title"),
                author = getString("author"),
                format = getString("format"),
                relativePath = getString("relative_path"),
                addedAt = getLong("added_at"),
                lastOpenedAt = lastOpened
            ))
        }
    }
}
