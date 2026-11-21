package ir.mrmabrosia.atlas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

private val AtlasLight = lightColorScheme(
    primary = Color(0xFF111111),
    onPrimary = Color(0xFFFFFFFF),
    background = Color(0xFFF5F5F2),
    onBackground = Color(0xFF111111),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF111111),
    secondary = Color(0xFF777777),
    outline = Color(0xFFE6E6E2)
)

private val AtlasDark = darkColorScheme(
    primary = Color(0xFFF5F5F2),
    onPrimary = Color(0xFF111111),
    background = Color(0xFF111111),
    onBackground = Color(0xFFF5F5F2),
    surface = Color(0xFF1B1B1B),
    onSurface = Color(0xFFF5F5F2),
    secondary = Color(0xFF999999),
    outline = Color(0xFF303030)
)

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "ATLAS"
    ) {
        AtlasApp()
    }
}

@Composable
private fun AtlasApp() {
    var darkMode by remember { mutableStateOf(false) }
    var selectedSection by remember { mutableStateOf("Home") }

    MaterialTheme(colorScheme = if (darkMode) AtlasDark else AtlasLight) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .width(236.dp)
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(22.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text("✧  ATLAS", style = MaterialTheme.typography.headlineSmall)
                Text(
                    "YOUR WORLD OF KNOWLEDGE",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(Modifier.padding(8.dp))
                listOf("Home", "Library", "Collections", "Reading", "Writing Studio").forEach { section ->
                    Surface(
                        onClick = { selectedSection = section },
                        shape = RoundedCornerShape(10.dp),
                        color = if (selectedSection == section) MaterialTheme.colorScheme.background
                        else Color.Transparent
                    ) {
                        Text(
                            section,
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 11.dp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                Spacer(Modifier.weight(1f))
                Surface(
                    onClick = { darkMode = !darkMode },
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Text(
                        if (darkMode) "Switch to light mode" else "Switch to dark mode",
                        modifier = Modifier.fillMaxWidth().padding(12.dp)
                    )
                }
                Text("AMBROSIA STUDIO", style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary)
            }

            Column(
                modifier = Modifier.fillMaxSize().padding(40.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                Text(selectedSection.uppercase(), style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.secondary)
                Text(
                    if (selectedSection == "Home") "A little more room to think."
                    else selectedSection,
                    style = MaterialTheme.typography.headlineLarge
                )
                Text(
                    "Your library, notes, and ideas — together in one quiet space.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.secondary
                )
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(28.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("Your space is ready.", style = MaterialTheme.typography.titleLarge)
                            Text(
                                "The desktop foundation is in place. Library import and reading tools are coming next.",
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }
            }
        }
    }
}
