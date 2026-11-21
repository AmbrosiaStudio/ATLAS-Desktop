package ir.mrmabrosia.atlas.data

import java.nio.file.Files
import java.nio.file.Path
import java.sql.Connection
import java.sql.DriverManager

class AtlasDatabase private constructor(private val databasePath: Path) {
    init {
        Files.createDirectories(databasePath.parent)
        connection().use { connection ->
            connection.createStatement().use { statement ->
                statement.executeUpdate(
                    """
                    CREATE TABLE IF NOT EXISTS books (
                        id TEXT PRIMARY KEY NOT NULL,
                        title TEXT NOT NULL,
                        author TEXT NOT NULL,
                        format TEXT NOT NULL,
                        relative_path TEXT NOT NULL UNIQUE,
                        added_at INTEGER NOT NULL,
                        last_opened_at INTEGER
                    )
                    """.trimIndent()
                )
                statement.executeUpdate("CREATE INDEX IF NOT EXISTS idx_books_title ON books(title COLLATE NOCASE)")
                statement.executeUpdate("CREATE INDEX IF NOT EXISTS idx_books_last_opened ON books(last_opened_at)")
            }
        }
    }

    fun connection(): Connection = DriverManager.getConnection("jdbc:sqlite:$databasePath")

    companion object {
        fun openDefault(): AtlasDatabase {
            val appDirectory = Path.of(System.getProperty("user.home"), ".atlas")
            return AtlasDatabase(appDirectory.resolve("atlas.db"))
        }
    }
}
