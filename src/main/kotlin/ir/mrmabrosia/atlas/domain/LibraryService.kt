package ir.mrmabrosia.atlas.domain

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.util.UUID

class LibraryService(
    private val repository: BookRepository,
    private val libraryDirectory: Path
) {
    private val supportedExtensions = setOf("epub", "pdf", "txt", "md", "markdown", "fb2", "cbz", "mp3", "m4a", "m4b", "flac", "ogg")

    fun importFiles(files: List<Path>): ImportResult {
        Files.createDirectories(libraryDirectory)
        var imported = 0
        val rejected = mutableListOf<String>()

        files.forEach { source ->
            val extension = source.fileName.toString().substringAfterLast('.', "").lowercase()
            if (!Files.isRegularFile(source) || extension !in supportedExtensions) {
                rejected += source.fileName.toString()
                return@forEach
            }
            try {
                val id = UUID.randomUUID().toString()
                val safeName = source.fileName.toString().replace(Regex("[^A-Za-z0-9._-]"), "_")
                val storedName = "$id-$safeName"
                val destination = libraryDirectory.resolve(storedName)
                Files.copy(source, destination, StandardCopyOption.COPY_ATTRIBUTES)
                repository.insert(Book(
                    id = id,
                    title = source.fileName.toString().substringBeforeLast('.').ifBlank { source.fileName.toString() },
                    author = "Unknown author",
                    format = extension.uppercase(),
                    relativePath = "library/$storedName",
                    addedAt = System.currentTimeMillis()
                ))
                imported++
            } catch (_: Exception) {
                rejected += source.fileName.toString()
            }
        }
        return ImportResult(imported, rejected)
    }

    fun resolve(book: Book): Path = libraryDirectory.parent.resolve(book.relativePath).normalize()

    fun open(book: Book): Path {
        repository.markOpened(book.id)
        return resolve(book)
    }

    data class ImportResult(val imported: Int, val rejected: List<String>)
}
