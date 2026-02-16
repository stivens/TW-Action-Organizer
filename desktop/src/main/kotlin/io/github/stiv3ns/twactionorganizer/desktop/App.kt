package io.github.stiv3ns.twactionorganizer.desktop

import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.stiv3ns.twactionorganizer.desktop.state.AppState
import io.github.stiv3ns.twactionorganizer.desktop.theme.AppTheme
import io.github.stiv3ns.twactionorganizer.desktop.ui.LogPanel
import io.github.stiv3ns.twactionorganizer.desktop.ui.SidebarPanel
import io.github.stiv3ns.twactionorganizer.desktop.ui.WorkAreaPanel

@Composable
fun App() {
    val scope = rememberCoroutineScope()
    val appState = remember { AppState(scope) }

    AppTheme(darkTheme = true) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top section: sidebar + work area
            Row(modifier = Modifier.weight(1f)) {
                SidebarPanel(
                    appState = appState,
                    modifier = Modifier.fillMaxHeight()
                )

                VerticalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                WorkAreaPanel(
                    appState = appState,
                    modifier = Modifier.weight(1f).fillMaxHeight()
                )
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outline)

            // Bottom section: log panel
            LogPanel(
                logStore = appState.logStore,
                modifier = Modifier.height(160.dp)
            )
        }
    }
}
