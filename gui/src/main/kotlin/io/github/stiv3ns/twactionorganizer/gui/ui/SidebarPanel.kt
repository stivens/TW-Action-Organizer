package io.github.stiv3ns.twactionorganizer.gui.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.stiv3ns.twactionorganizer.gui.state.AppState
import io.github.stiv3ns.twactionorganizer.gui.state.Category
import io.github.stiv3ns.twactionorganizer.gui.theme.TwColors
import io.github.stiv3ns.twactionorganizer.gui.ui.components.*

@Composable
fun SidebarPanel(
    appState: AppState,
    modifier: Modifier = Modifier
) {
    var showWorldDialog by remember { mutableStateOf(false) }
    var showConcreteDialog by remember { mutableStateOf(false) }
    var showFakeDialog by remember { mutableStateOf(false) }
    var showDemolitionDialog by remember { mutableStateOf(false) }
    var showTargetGroupDialog by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier.fillMaxHeight(),
        color = TwColors.woodFrame,
    ) {
        Column(
            modifier = Modifier
                .width(260.dp)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
                .padding(vertical = 8.dp)
        ) {
            // World section
            SectionHeader("World", buttonLabel = "Setup", onAdd = { showWorldDialog = true })
            CategoryItem(
                label = appState.world?.domain ?: "(not set)",
                isSelected = appState.selectedCategory is Category.WorldConfig,
                onClick = { appState.selectedCategory = Category.WorldConfig }
            )

            Spacer(Modifier.height(12.dp))
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 12.dp),
                color = TwColors.woodLight
            )
            Spacer(Modifier.height(8.dp))

            // Resources section
            SectionHeader("Resources")

            CategoryItem(
                label = "Concrete (${appState.concreteResources.villageCount} villages)",
                isSelected = appState.selectedCategory is Category.ConcreteResources,
                onClick = { appState.selectedCategory = Category.ConcreteResources },
                onRemove = if (appState.concreteResources.villageCount > 0) {
                    { appState.clearConcreteResources() }
                } else null
            )
            // Add button for concrete
            if (appState.concreteResources.villageCount == 0) {
                TextButton(
                    onClick = { showConcreteDialog = true },
                    modifier = Modifier.padding(start = 28.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                ) {
                    Text("+ Load concrete resources", style = MaterialTheme.typography.labelSmall)
                }
            }

            CategoryItem(
                label = "Fake (${appState.fakeResources.villageCount} villages)",
                isSelected = appState.selectedCategory is Category.FakeResources,
                onClick = { appState.selectedCategory = Category.FakeResources },
                onRemove = if (appState.fakeResources.villageCount > 0) {
                    { appState.clearFakeResources() }
                } else null
            )
            if (appState.fakeResources.villageCount == 0) {
                TextButton(
                    onClick = { showFakeDialog = true },
                    modifier = Modifier.padding(start = 28.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                ) {
                    Text("+ Load fake resources", style = MaterialTheme.typography.labelSmall)
                }
            }

            CategoryItem(
                label = "Demolition (${appState.demolitionResources.villageCount} villages)",
                isSelected = appState.selectedCategory is Category.DemolitionResources,
                onClick = { appState.selectedCategory = Category.DemolitionResources },
                onRemove = if (appState.demolitionResources.villageCount > 0) {
                    { appState.clearDemolitionResources() }
                } else null
            )
            if (appState.demolitionResources.villageCount == 0) {
                TextButton(
                    onClick = { showDemolitionDialog = true },
                    modifier = Modifier.padding(start = 28.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                ) {
                    Text("+ Load demolition resources", style = MaterialTheme.typography.labelSmall)
                }
            }

            Spacer(Modifier.height(12.dp))
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 12.dp),
                color = TwColors.woodLight
            )
            Spacer(Modifier.height(8.dp))

            // Target Groups section
            SectionHeader("Target Groups", onAdd = { showTargetGroupDialog = true })

            if (appState.targetGroups.isEmpty()) {
                Text(
                    "No target groups yet",
                    style = MaterialTheme.typography.bodySmall,
                    color = TwColors.gold,
                    modifier = Modifier.padding(start = 28.dp, top = 4.dp)
                )
            }

            appState.targetGroups.forEach { group ->
                CategoryItem(
                    label = "${group.name} (${group.villageCount}v)",
                    isSelected = appState.selectedCategory == Category.TargetGroupItem(group),
                    onClick = { appState.selectedCategory = Category.TargetGroupItem(group) },
                    onRemove = { appState.removeTargetGroup(group) },
                    indent = 1
                )
            }

            Spacer(Modifier.height(12.dp))
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 12.dp),
                color = TwColors.woodLight
            )
            Spacer(Modifier.height(8.dp))

            // Assignments section
            CategoryItem(
                label = "Assignments (${appState.assignments.size})",
                isSelected = appState.selectedCategory is Category.Assignments,
                onClick = { appState.selectedCategory = Category.Assignments }
            )

            Spacer(Modifier.height(12.dp))
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 12.dp),
                color = TwColors.woodLight
            )
            Spacer(Modifier.height(8.dp))

            // Map section
            CategoryItem(
                label = "Map",
                isSelected = appState.selectedCategory is Category.MapView,
                onClick = { appState.selectedCategory = Category.MapView }
            )

            Spacer(Modifier.height(16.dp))

            // Execute button
            Button(
                onClick = { appState.execute() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                enabled = !appState.isExecuting && appState.world != null
            ) {
                if (appState.isExecuting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Executing...")
                } else {
                    Text("Execute")
                }
            }
        }
    }

    // Dialogs
    if (showWorldDialog) {
        WorldDialog(
            onDismiss = { showWorldDialog = false },
            appState = appState
        )
    }
    if (showConcreteDialog) {
        ResourcesDialog(
            title = "Load Concrete Resources",
            onDismiss = { showConcreteDialog = false },
            appState = appState,
            onResult = { appState.updateConcreteResources(it) }
        )
    }
    if (showFakeDialog) {
        ResourcesDialog(
            title = "Load Fake Resources",
            onDismiss = { showFakeDialog = false },
            appState = appState,
            onResult = { appState.updateFakeResources(it) }
        )
    }
    if (showDemolitionDialog) {
        ResourcesDialog(
            title = "Load Demolition Resources",
            onDismiss = { showDemolitionDialog = false },
            appState = appState,
            onResult = { appState.updateDemolitionResources(it) }
        )
    }
    if (showTargetGroupDialog) {
        TargetGroupDialog(
            onDismiss = { showTargetGroupDialog = false },
            appState = appState
        )
    }
}
