package ir.mrmabrosia.atlas

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import ir.mrmabrosia.atlas.data.AtlasDatabase
import ir.mrmabrosia.atlas.data.SqliteBookRepository
import ir.mrmabrosia.atlas.domain.LibraryService
import ir.mrmabrosia.atlas.ui.AtlasApp
import java.nio.file.Path

fun main() = application {
    val database = AtlasDatabase.openDefault()
    val repository = SqliteBookRepository(database)
    val appDirectory = Path.of(System.getProperty("user.home"), ".atlas")
    val library = LibraryService(repository, appDirectory.resolve("library"))

    Window(
        onCloseRequest = ::exitApplication,
        title = "ATLAS",
        state = WindowState(size = DpSize(1280.dp, 820.dp))
    ) {
        AtlasApp(repository, library)
    }
}
