package ir.mrmabrosia.atlas.domain

data class Book(
    val id: String,
    val title: String,
    val author: String,
    val format: String,
    val relativePath: String,
    val addedAt: Long,
    val lastOpenedAt: Long? = null
)

interface BookRepository {
    fun all(): List<Book>
    fun search(query: String): List<Book>
    fun insert(book: Book)
    fun markOpened(id: String)
    fun recentlyOpened(limit: Int = 8): List<Book>
    fun count(): Int
}
