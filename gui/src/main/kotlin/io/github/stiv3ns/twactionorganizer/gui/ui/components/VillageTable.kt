package io.github.stiv3ns.twactionorganizer.gui.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.stiv3ns.twactionorganizer.core.villages.TargetVillage
import io.github.stiv3ns.twactionorganizer.core.villages.Village
import io.github.stiv3ns.twactionorganizer.gui.theme.TwColors

data class ColumnDef(
    val header: String,
    val weight: Float,
    val valueExtractor: (Village) -> String
)

@Composable
fun AllyVillageTable(
    villages: Collection<Village>,
    modifier: Modifier = Modifier
) {
    val columns = listOf(
        ColumnDef("Owner", 2f) { it.ownerNickname },
        ColumnDef("X", 0.7f) { it.x.toString() },
        ColumnDef("Y", 0.7f) { it.y.toString() },
        ColumnDef("ID", 1f) { it.id.toString() },
    )
    VillageTableInternal(villages.toList(), columns, modifier)
}

@Composable
fun TargetVillageTable(
    villages: Collection<TargetVillage>,
    modifier: Modifier = Modifier
) {
    val columns = listOf(
        ColumnDef("Owner", 2f) { it.ownerNickname },
        ColumnDef("X", 0.7f) { it.x.toString() },
        ColumnDef("Y", 0.7f) { it.y.toString() },
        ColumnDef("ID", 1f) { it.id.toString() },
        ColumnDef("Attacks", 0.8f) { (it as? TargetVillage)?.numberOfAttacks?.toString() ?: "" },
        ColumnDef("Delay", 0.8f) { v ->
            val delay = (v as? TargetVillage)?.delayInMinutes ?: 0
            if (delay > 0) "${delay}m" else ""
        },
    )
    VillageTableInternal(villages.toList(), columns, modifier)
}

@Composable
private fun VillageTableInternal(
    villages: List<Village>,
    columns: List<ColumnDef>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Header row — golden-brown like TW table headers
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(TwColors.tableHeader)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            columns.forEach { col ->
                Text(
                    text = col.header,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = TwColors.woodDark,
                    modifier = Modifier.weight(col.weight),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        HorizontalDivider(color = TwColors.woodLight)

        // Data rows — alternating parchment shades like TW tables
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(villages.size) { index ->
                val village = villages[index]
                val rowBg = if (index % 2 == 0) TwColors.parchmentLight else TwColors.parchmentMid
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(rowBg)
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    columns.forEach { col ->
                        Text(
                            text = col.valueExtractor(village),
                            style = MaterialTheme.typography.bodySmall,
                            color = TwColors.inkDark,
                            modifier = Modifier.weight(col.weight),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                HorizontalDivider(
                    color = TwColors.parchmentDark
                )
            }
        }
    }
}
