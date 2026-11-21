package ir.mrmabrosia.atlas.platform

import java.io.File
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

object BookFilePicker {
    fun chooseFiles(): List<java.nio.file.Path> {
        val chooser = JFileChooser().apply {
            dialogTitle = "Add books to ATLAS"
            isMultiSelectionEnabled = true
            fileSelectionMode = JFileChooser.FILES_ONLY
            isAcceptAllFileFilterUsed = true
            addChoosableFileFilter(
                FileNameExtensionFilter(
                    "Books and audio",
                    "epub", "pdf", "txt", "md", "markdown", "fb2", "cbz", "mp3", "m4a", "m4b", "flac", "ogg"
                )
            )
        }
        if (chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) return emptyList()
        return chooser.selectedFiles.map(File::toPath)
    }
}
