@file:OptIn(kotlinx.coroutines.ObsoleteCoroutinesApi::class)

package io.github.stiv3ns.twactionorganizer.desktop.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogWindow
import io.github.stiv3ns.twactionorganizer.core.Resources
import io.github.stiv3ns.twactionorganizer.core.TargetGroup
import io.github.stiv3ns.twactionorganizer.core.World
import io.github.stiv3ns.twactionorganizer.core.assigners.AssignerType
import io.github.stiv3ns.twactionorganizer.core.parsers.AllyParserWithDynamicOwnerResolution
import io.github.stiv3ns.twactionorganizer.core.parsers.TargetParser
import io.github.stiv3ns.twactionorganizer.desktop.state.AppState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun WorldDialog(
    onDismiss: () -> Unit,
    appState: AppState
) {
    var domain by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    DialogWindow(
        onCloseRequest = onDismiss,
        title = "Set World"
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("World Domain", style = MaterialTheme.typography.titleMedium)

                OutlinedTextField(
                    value = domain,
                    onValueChange = { domain = it },
                    label = { Text("Domain (e.g. pl150.plemiona.pl)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !loading
                )

                if (error != null) {
                    Text(
                        text = error!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss, enabled = !loading) {
                        Text("Cancel")
                    }
                    Spacer(Modifier.width(8.dp))
                    Button(
                        onClick = {
                            loading = true
                            error = null
                            scope.launch(Dispatchers.IO) {
                                try {
                                    val world = World(domain.trim())
                                    appState.updateWorld(world)
                                    onDismiss()
                                } catch (e: Exception) {
                                    error = e.message ?: "Failed to load world"
                                    loading = false
                                }
                            }
                        },
                        enabled = domain.isNotBlank() && !loading
                    ) {
                        if (loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Connect")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ResourcesDialog(
    title: String,
    onDismiss: () -> Unit,
    appState: AppState,
    onResult: (Resources) -> Unit
) {
    var text by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    DialogWindow(
        onCloseRequest = onDismiss,
        title = title
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(title, style = MaterialTheme.typography.titleMedium)

                Text(
                    "Paste village coordinates (one per line, format: XXX|YYY)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Village data") },
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    enabled = !loading
                )

                if (error != null) {
                    Text(
                        text = error!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss, enabled = !loading) {
                        Text("Cancel")
                    }
                    Spacer(Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val world = appState.world
                            if (world == null) {
                                error = "Set a world first"
                                return@Button
                            }
                            loading = true
                            error = null
                            scope.launch(Dispatchers.IO) {
                                try {
                                    val villages = AllyParserWithDynamicOwnerResolution
                                        .parse(world = world, plainText = text)
                                    val resources = Resources.fromVillageCollection(villages)
                                    onResult(resources)
                                    onDismiss()
                                } catch (e: Exception) {
                                    error = e.message ?: "Failed to parse"
                                    loading = false
                                }
                            }
                        },
                        enabled = text.isNotBlank() && !loading
                    ) {
                        if (loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Parse")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TargetGroupDialog(
    onDismiss: () -> Unit,
    appState: AppState
) {
    var name by remember { mutableStateOf("") }
    var text by remember { mutableStateOf("") }
    var attacksPerVillage by remember { mutableStateOf("1") }
    var selectedType by remember { mutableStateOf(AssignerType.RANDOMIZED_RAM) }
    var typeMenuExpanded by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    DialogWindow(
        onCloseRequest = onDismiss,
        title = "Add Target Group"
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Add Target Group", style = MaterialTheme.typography.titleMedium)

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Group name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !loading
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = attacksPerVillage,
                        onValueChange = { attacksPerVillage = it.filter { c -> c.isDigit() } },
                        label = { Text("Attacks/village") },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        enabled = !loading
                    )

                    Box(modifier = Modifier.weight(1f)) {
                        OutlinedTextField(
                            value = selectedType.name,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Type") },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth(),
                            enabled = !loading
                        )
                        DropdownMenu(
                            expanded = typeMenuExpanded,
                            onDismissRequest = { typeMenuExpanded = false }
                        ) {
                            AssignerType.entries.forEach { type ->
                                DropdownMenuItem(
                                    text = { Text(type.name) },
                                    onClick = {
                                        selectedType = type
                                        typeMenuExpanded = false
                                    }
                                )
                            }
                        }
                        // Invisible click overlay
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .padding(top = 8.dp)
                                .let { mod ->
                                    mod
                                }
                        ) {
                            androidx.compose.foundation.layout.Spacer(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .let { m ->
                                        @Suppress("DEPRECATION")
                                        m
                                    }
                            )
                        }
                    }
                }

                // Clickable area for dropdown
                TextButton(
                    onClick = { typeMenuExpanded = true },
                    enabled = !loading
                ) {
                    Text("Change assigner type: ${selectedType.name}")
                }

                Text(
                    "Paste target village coordinates (one per line, format: XXX|YYY)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Target villages") },
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    enabled = !loading
                )

                if (error != null) {
                    Text(
                        text = error!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss, enabled = !loading) {
                        Text("Cancel")
                    }
                    Spacer(Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val world = appState.world
                            if (world == null) {
                                error = "Set a world first"
                                return@Button
                            }
                            val apv = attacksPerVillage.toIntOrNull()
                            if (apv == null || apv < 1) {
                                error = "Invalid attacks per village"
                                return@Button
                            }
                            if (name.isBlank()) {
                                error = "Name is required"
                                return@Button
                            }
                            loading = true
                            error = null
                            scope.launch(Dispatchers.IO) {
                                try {
                                    val villages = TargetParser.parse(
                                        plainText = text,
                                        attacksPerVillage = apv,
                                        world = world
                                    )
                                    val group = TargetGroup(
                                        name = name.trim(),
                                        type = selectedType,
                                        villages = villages
                                    )
                                    appState.addTargetGroup(group)
                                    onDismiss()
                                } catch (e: Exception) {
                                    error = e.message ?: "Failed to parse targets"
                                    loading = false
                                }
                            }
                        },
                        enabled = text.isNotBlank() && name.isNotBlank() && !loading
                    ) {
                        if (loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Add")
                        }
                    }
                }
            }
        }
    }
}
