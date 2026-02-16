package io.github.stiv3ns.twactionorganizer.desktop.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.stiv3ns.twactionorganizer.core.Assignment
import io.github.stiv3ns.twactionorganizer.core.Resources
import io.github.stiv3ns.twactionorganizer.core.TargetGroup
import io.github.stiv3ns.twactionorganizer.core.World
import io.github.stiv3ns.twactionorganizer.desktop.state.AppState
import io.github.stiv3ns.twactionorganizer.desktop.state.Category
import io.github.stiv3ns.twactionorganizer.desktop.ui.components.AllyVillageTable
import io.github.stiv3ns.twactionorganizer.desktop.ui.components.TargetVillageTable

@Composable
fun WorkAreaPanel(
    appState: AppState,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        when (val cat = appState.selectedCategory) {
            is Category.WorldConfig -> WorldView(appState.world)
            is Category.ConcreteResources -> ResourcesView("Concrete Resources", appState.concreteResources)
            is Category.FakeResources -> ResourcesView("Fake Resources", appState.fakeResources)
            is Category.DemolitionResources -> ResourcesView("Demolition Resources", appState.demolitionResources)
            is Category.TargetGroupItem -> TargetGroupView(cat.group)
            is Category.Assignments -> AssignmentsView(appState.assignments)
        }
    }
}

@Composable
private fun WorldView(world: World?) {
    if (world == null) {
        EmptyState("No world configured. Use the sidebar to set one.")
        return
    }

    Column(modifier = Modifier.padding(20.dp)) {
        Text(
            "World Configuration",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(16.dp))

        InfoCard {
            InfoRow("Domain", world.domain)
            InfoRow("World Speed", world.worldSpeed.toString())
            InfoRow("Unit Speed", world.unitSpeed.toString())
            InfoRow("Combined Speed", world.speed.toString())
            InfoRow("Max Noble Range", world.maxNobleRange.toString())
            InfoRow("Night Bonus End Hour", "${world.nightBonusEndHour}:00")
            InfoRow("Total Villages", world.villages.size.toString())
        }
    }
}

@Composable
private fun ResourcesView(title: String, resources: Resources) {
    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Text(
            title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(12.dp))

        if (resources.villageCount == 0) {
            EmptyState("No resources loaded. Use the sidebar to add them.")
            return
        }

        InfoCard {
            InfoRow("Players", resources.playerCount.toString())
            InfoRow("Villages", resources.villageCount.toString())
            InfoRow("Nobles", resources.nobleCount.toString())
        }

        Spacer(Modifier.height(16.dp))

        AllyVillageTable(
            villages = resources.villages,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun TargetGroupView(group: TargetGroup) {
    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Text(
            group.name,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(12.dp))

        InfoCard {
            InfoRow("Type", group.type.name)
            InfoRow("Villages", group.villageCount.toString())
            InfoRow("Total Attacks", group.totalAttackCount.toString())
        }

        Spacer(Modifier.height(16.dp))

        TargetVillageTable(
            villages = group.villages,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun AssignmentsView(assignments: List<Assignment>) {
    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Text(
            "Assignments",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(12.dp))

        if (assignments.isEmpty()) {
            EmptyState("No assignments yet. Configure resources and targets, then execute.")
            return
        }

        Text(
            "${assignments.size} assignments generated",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(12.dp))

        // Assignments table header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TableHeader("From", Modifier.weight(1.5f))
            TableHeader("To", Modifier.weight(1.5f))
            TableHeader("Type", Modifier.weight(1.5f))
            TableHeader("Distance", Modifier.weight(1f))
            TableHeader("Delay", Modifier.weight(0.8f))
        }
        HorizontalDivider(color = MaterialTheme.colorScheme.outline)

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(assignments) { assignment ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        assignment.departure.toString(),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.weight(1.5f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        assignment.destination.toString(),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.weight(1.5f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        assignment.type.name,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.weight(1.5f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        "%.1f".format(kotlin.math.sqrt(assignment.squaredDistance.toDouble())),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.weight(1f),
                        maxLines = 1
                    )
                    Text(
                        if (assignment.delayInMinutes > 0) "${assignment.delayInMinutes}m" else "",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.weight(0.8f),
                        maxLines = 1
                    )
                }
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                )
            }
        }
    }
}

@Composable
private fun TableHeader(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun EmptyState(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun InfoCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            content = content
        )
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
