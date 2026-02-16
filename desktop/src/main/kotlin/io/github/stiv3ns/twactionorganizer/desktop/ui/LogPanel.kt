package io.github.stiv3ns.twactionorganizer.desktop.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import io.github.stiv3ns.twactionorganizer.desktop.state.LogEntry
import io.github.stiv3ns.twactionorganizer.desktop.state.LogLevel
import io.github.stiv3ns.twactionorganizer.desktop.state.LogStore
import io.github.stiv3ns.twactionorganizer.desktop.theme.TwColors

@Composable
fun LogPanel(
    logStore: LogStore,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val entries = logStore.entries

    LaunchedEffect(entries.size) {
        if (entries.isNotEmpty()) {
            listState.animateScrollToItem(entries.lastIndex)
        }
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = TwColors.woodDark,
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(TwColors.woodFrame)
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Logs",
                    style = MaterialTheme.typography.labelLarge,
                    color = TwColors.goldBright
                )
                Spacer(Modifier.weight(1f))
                Text(
                    "${entries.size} entries",
                    style = MaterialTheme.typography.labelSmall,
                    color = TwColors.gold
                )
            }

            HorizontalDivider(color = TwColors.woodLight)

            if (entries.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No logs yet",
                        style = MaterialTheme.typography.bodySmall,
                        color = TwColors.gold
                    )
                }
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(entries) { entry ->
                        LogEntryRow(entry)
                    }
                }
            }
        }
    }
}

@Composable
private fun LogEntryRow(entry: LogEntry) {
    val levelColor = when (entry.level) {
        LogLevel.INFO -> TwColors.parchment
        LogLevel.WARN -> TwColors.goldBright
        LogLevel.ERROR -> TwColors.redLight
        LogLevel.REPORT -> TwColors.blueLight
    }

    val levelTag = when (entry.level) {
        LogLevel.INFO -> "INFO"
        LogLevel.WARN -> "WARN"
        LogLevel.ERROR -> "ERR "
        LogLevel.REPORT -> "RPT "
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = entry.timestamp,
            style = MaterialTheme.typography.bodySmall,
            fontFamily = FontFamily.Monospace,
            color = TwColors.gold,
            modifier = Modifier.width(64.dp)
        )
        Text(
            text = levelTag,
            style = MaterialTheme.typography.labelSmall,
            fontFamily = FontFamily.Monospace,
            color = levelColor,
            modifier = Modifier.width(40.dp)
        )
        Text(
            text = entry.message,
            style = MaterialTheme.typography.bodySmall,
            color = levelColor,
            modifier = Modifier.weight(1f)
        )
    }
}
