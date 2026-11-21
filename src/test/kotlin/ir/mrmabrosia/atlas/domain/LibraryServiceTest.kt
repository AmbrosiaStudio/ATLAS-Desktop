package ir.mrmabrosia.atlas.domain

import java.nio.file.Files
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LibraryServiceTest {
    @Test
    fun `import copies supported files and preserves the original`() {
        val root = Files.createTempDirectory("atlas-library-test")
        try {
            val source = root.resolve("A small book.txt")
            Files.writeString(source, "Original content")
            val repository = InMemoryBookRepository()
            val service = LibraryService(repository, root.resolve(".atlas/library"))

            val result = service.importFiles(listOf(source))

            assertEquals(1, result.imported)
            assertTrue(result.rejected.isEmpty())
            assertEquals("Original content", Files.readString(source))
            assertEquals(1, repository.all().size)

            val imported = repository.all().single()
            assertEquals("A small book", imported.title)
            assertEquals("TXT", imported.format)
            assertEquals("Original content", Files.readString(service.resolve(imported)))
        } finally {
            root.toFile().deleteRecursively()
        }
    }

    @Test
    fun `unsupported files are rejected without entering the catalogue`() {
        val root = Files.createTempDirectory("atlas-library-test")
        try {
            val source = root.resolve("payload.exe")
            Files.writeString(source, "not a book")
            val repository = InMemoryBookRepository()
            val service = LibraryService(repository, root.resolve(".atlas/library"))

            val result = service.importFiles(listOf(source))

            assertEquals(0, result.imported)
            assertEquals(listOf("payload.exe"), result.rejected)
            assertTrue(repository.all().isEmpty())
        } finally {
            root.toFile().deleteRecursively()
        }
    }

    private class InMemoryBookRepository : BookRepository {
        private val books = mutableListOf<Book>()

        override fun all(): List<Book> = books.toList()
        override fun search(query: String): List<Book> =
            books.filter { it.title.contains(query, ignoreCase = true) }
        override fun insert(book: Book) { books += book }
        override fun markOpened(id: String) = Unit
        override fun recentlyOpened(limit: Int): List<Book> = emptyList()
        override fun count(): Int = books.size
    }
}
