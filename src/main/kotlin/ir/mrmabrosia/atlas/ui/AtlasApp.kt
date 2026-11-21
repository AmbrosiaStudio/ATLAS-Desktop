package ir.mrmabrosia.atlas.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ir.mrmabrosia.atlas.domain.Book
import ir.mrmabrosia.atlas.domain.BookRepository
import ir.mrmabrosia.atlas.domain.LibraryService
import ir.mrmabrosia.atlas.platform.BookFilePicker
import java.awt.Desktop
import java.nio.file.Files

private val AtlasLight = lightColorScheme(
    primary = Color(0xFF111111), onPrimary = Color.White,
    background = Color(0xFFF5F5F2), onBackground = Color(0xFF111111),
    surface = Color.White, onSurface = Color(0xFF111111),
    secondary = Color(0xFF777777), outline = Color(0xFFE6E6E2)
)
private val AtlasDark = darkColorScheme(
    primary = Color(0xFFF5F5F2), onPrimary = Color(0xFF111111),
    background = Color(0xFF111111), onBackground = Color(0xFFF5F5F2),
    surface = Color(0xFF1B1B1B), onSurface = Color(0xFFF5F5F2),
    secondary = Color(0xFF999999), outline = Color(0xFF303030)
)

@Composable
fun AtlasApp(repository: BookRepository, library: LibraryService) {
    var darkMode by remember { mutableStateOf(false) }
    var selectedSection by remember { mutableStateOf("Home") }
    var books by remember { mutableStateOf(emptyList<Book>()) }
    var search by remember { mutableStateOf("") }
    var readerText by remember { mutableStateOf<String?>(null) }
    var readerTitle by remember { mutableStateOf("Reader") }
    var notice by remember { mutableStateOf<String?>(null) }

    fun refresh() { books = repository.search(search) }
    LaunchedEffect(Unit) { refresh() }
    LaunchedEffect(search) { refresh() }

    MaterialTheme(colorScheme = if (darkMode) AtlasDark else AtlasLight) {
        Row(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
            Column(
                Modifier.width(250.dp).fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface).padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(9.dp)
            ) {
                Text("✧  ATLAS", style = MaterialTheme.typography.headlineSmall)
                Text("YOUR WORLD OF KNOWLEDGE", style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary)
                Spacer(Modifier.height(22.dp))
                listOf("Home", "Library", "Collections", "Reading", "Writing Studio").forEach { section ->
                    val active = selectedSection == section
                    Surface(
                        onClick = { selectedSection = section },
                        shape = RoundedCornerShape(10.dp),
                        color = if (active) MaterialTheme.colorScheme.background else Color.Transparent
                    ) {
                        Text(section, Modifier.fillMaxWidth().padding(horizontal = 13.dp, vertical = 12.dp))
                    }
                }
                Spacer(Modifier.weight(1f))
                Surface(
                    onClick = { darkMode = !darkMode },
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Text(if (darkMode) "Switch to light mode" else "Switch to dark mode",
                        Modifier.fillMaxWidth().padding(12.dp))
                }
                Text("AMBROSIA STUDIO", style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary)
            }

            Column(
                Modifier.fillMaxSize().padding(horizontal = 42.dp, vertical = 34.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                when (selectedSection) {
                    "Home" -> HomeScreen(books, onImport = { importBooks(library, ::refresh) { notice = it } },
                        onOpen = { openBook(it, library, { text, title -> readerText = text; readerTitle = title }, { notice = it }) })
                    "Library" -> LibraryScreen(books, search, { search = it },
                        onImport = { importBooks(library, ::refresh) { notice = it } },
                        onOpen = { openBook(it, library, { text, title -> readerText = text; readerTitle = title }, { notice = it }) })
                    "Collections" -> CollectionsScreen(books)
                    "Reading" -> ReadingScreen(repository.recentlyOpened(),
                        onOpen = { openBook(it, library, { text, title -> readerText = text; readerTitle = title }, { notice = it }) })
                    "Writing Studio" -> PlaceholderScreen("Writing Studio",
                        "A focused space for drafts, notes, and long-form writing is planned for a later milestone.")
                }
            }
        }

        readerText?.let { content ->
            AlertDialog(
                onDismissRequest = { readerText = null },
                confirmButton = { TextButton(onClick = { readerText = null }) { Text("Close") } },
                title = { Text(readerTitle) },
                text = {
                    Column(Modifier.width(640.dp).height(520.dp).verticalScroll(rememberScrollState())) {
                        Text(content, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            )
        }
        notice?.let { message ->
            AlertDialog(
                onDismissRequest = { notice = null },
                confirmButton = { TextButton(onClick = { notice = null }) { Text("OK") } },
                title = { Text("ATLAS") },
                text = { Text(message) }
            )
        }
    }
}

private fun importBooks(library: LibraryService, refresh: () -> Unit, notify: (String) -> Unit) {
    val files = BookFilePicker.chooseFiles()
    if (files.isEmpty()) return
    val result = library.importFiles(files)
    refresh()
    notify(buildString {
        append("${result.imported} file(s) added to your library.")
        if (result.rejected.isNotEmpty()) append("\nSkipped: ${result.rejected.joinToString()}")
    })
}

private fun openBook(book: Book, library: LibraryService, showText: (String, String) -> Unit, notify: (String) -> Unit) {
    try {
        val file = library.open(book)
        if (book.format in setOf("TXT", "MD", "MARKDOWN")) {
            showText(Files.readString(file), book.title)
        } else if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().open(file.toFile())
        } else {
            notify("This format is not yet supported by the built-in reader. The original file remains safely stored in your ATLAS library.")
        }
    } catch (error: Exception) {
        notify("Couldn't open this item: ${error.message ?: "Unknown error"}")
    }
}

@Composable
private fun HomeScreen(books: List<Book>, onImport: () -> Unit, onOpen: (Book) -> Unit) {
    ScreenHeader("A little more room to think.", "Your library, notes, and ideas — together in one quiet space.", onImport)
    Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
        StatCard("IN YOUR LIBRARY", books.size.toString(), Modifier.weight(1f))
        StatCard("FORMATS", books.map { it.format }.distinct().size.toString(), Modifier.weight(1f))
        StatCard("RECENTLY ADDED", books.take(5).size.toString(), Modifier.weight(1f))
    }
    Text("Recently added", style = MaterialTheme.typography.titleLarge)
    if (books.isEmpty()) EmptyLibrary(onImport)
    else LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(books.take(6), key = { it.id }) { BookRow(it, onOpen) }
    }
}

@Composable
private fun LibraryScreen(
    books: List<Book>, search: String, onSearch: (String) -> Unit,
    onImport: () -> Unit, onOpen: (Book) -> Unit
) {
    ScreenHeader("Your library", "A home for the things you want to read and keep.", onImport)
    OutlinedTextField(value = search, onValueChange = onSearch, modifier = Modifier.fillMaxWidth(),
        singleLine = true, label = { Text("Search title, author, or format") })
    if (books.isEmpty()) EmptyLibrary(onImport)
    else LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(books, key = { it.id }) { BookRow(it, onOpen) }
    }
}

@Composable
private fun CollectionsScreen(books: List<Book>) {
    ScreenHeader("Collections", "Your library, grouped by format for now.", null)
    val groups = books.groupBy { it.format }.toSortedMap()
    if (groups.isEmpty()) Text("Your collections will appear here after you add books.")
    groups.forEach { (format, entries) ->
        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
            Row(Modifier.fillMaxWidth().padding(18.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(format, style = MaterialTheme.typography.titleMedium)
                Text("${entries.size} items", color = MaterialTheme.colorScheme.secondary)
            }
        }
    }
}

@Composable
private fun ReadingScreen(recent: List<Book>, onOpen: (Book) -> Unit) {
    ScreenHeader("Continue reading", "Pick up where you last opened a title.", null)
    if (recent.isEmpty()) Text("Nothing here yet. Open a book from your library and it will show up here.")
    else LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(recent, key = { it.id }) { BookRow(it, onOpen) }
    }
}

@Composable
private fun PlaceholderScreen(title: String, description: String) {
    ScreenHeader(title, description, null)
    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Text("Coming in a future milestone", Modifier.padding(24.dp), color = MaterialTheme.colorScheme.secondary)
    }
}

@Composable
private fun ScreenHeader(title: String, subtitle: String, onImport: (() -> Unit)?) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(7.dp)) {
            Text(title, style = MaterialTheme.typography.headlineLarge)
            Text(subtitle, color = MaterialTheme.colorScheme.secondary, style = MaterialTheme.typography.bodyLarge)
        }
        if (onImport != null) Button(onClick = onImport) { Text("＋  Add files") }
    }
}

@Composable
private fun StatCard(label: String, value: String, modifier: Modifier = Modifier) {
    Card(modifier, colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
            Text(value, style = MaterialTheme.typography.headlineMedium)
        }
    }
}

@Composable
private fun EmptyLibrary(onImport: () -> Unit) {
    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(Modifier.fillMaxWidth().padding(28.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("Your library starts here.", style = MaterialTheme.typography.titleLarge)
            Text("Import EPUB, PDF, TXT, Markdown, FB2, CBZ, and audio files. ATLAS keeps a copy in your local library and never changes the originals.",
                color = MaterialTheme.colorScheme.secondary)
            Button(onClick = onImport) { Text("Add your first files") }
        }
    }
}

@Composable
private fun BookRow(book: Book, onOpen: (Book) -> Unit) {
    Surface(onClick = { onOpen(book) }, shape = RoundedCornerShape(12.dp), color = MaterialTheme.colorScheme.surface) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 15.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(book.title, style = MaterialTheme.typography.titleMedium)
                Text(book.author, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
            }
            Text(book.format, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
        }
    }
}
