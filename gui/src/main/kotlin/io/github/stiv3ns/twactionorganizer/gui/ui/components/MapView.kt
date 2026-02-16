package io.github.stiv3ns.twactionorganizer.gui.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import io.github.stiv3ns.twactionorganizer.gui.state.AppState

private data class MapPoint(val x: Int, val y: Int, val color: Color)

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MapView(
    appState: AppState,
    modifier: Modifier = Modifier
) {
    val points = remember(
        appState.concreteResources,
        appState.fakeResources,
        appState.demolitionResources,
        appState.targetGroups.toList()
    ) {
        buildList {
            val blue = Color(0xFF4488FF)
            val darkBlue = Color(0xFF2266CC)
            val cyan = Color(0xFF44CCCC)

            appState.concreteResources.villages.forEach { v ->
                add(MapPoint(v.x, v.y, blue))
            }
            appState.fakeResources.villages.forEach { v ->
                add(MapPoint(v.x, v.y, darkBlue))
            }
            appState.demolitionResources.villages.forEach { v ->
                add(MapPoint(v.x, v.y, cyan))
            }

            val red = Color(0xFFFF3333)
            appState.targetGroups.forEach { group ->
                group.villages.forEach { v ->
                    add(MapPoint(v.x, v.y, red))
                }
            }
        }
    }

    if (points.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize().background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "No villages loaded. Add resources or target groups to see the map.",
                color = Color.Gray,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        return
    }

    val mapSize = 1000
    val minX = 0
    val maxX = mapSize - 1
    val minY = 0
    val maxY = mapSize - 1

    val rangeX = mapSize
    val rangeY = mapSize

    var zoom by remember { mutableStateOf(1f) }
    var panOffset by remember { mutableStateOf(Offset.Zero) }

    Column(modifier = modifier.fillMaxSize().background(Color.Black)) {
        // Legend
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF111111))
                .padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LegendDot(Color(0xFF4488FF), "Concrete")
            LegendDot(Color(0xFF2266CC), "Fake")
            LegendDot(Color(0xFF44CCCC), "Demolition")
            LegendDot(Color(0xFFFF3333), "Targets")
            Spacer(Modifier.weight(1f))
            Text(
                "Scroll to zoom, drag to pan",
                color = Color.Gray,
                style = MaterialTheme.typography.labelSmall
            )
        }

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .onPointerEvent(PointerEventType.Scroll) { event ->
                    val scrollDelta = event.changes.firstOrNull()?.scrollDelta?.y ?: 0f
                    val zoomFactor = if (scrollDelta < 0) 1.15f else 1f / 1.15f
                    zoom = (zoom * zoomFactor).coerceIn(0.2f, 30f)
                }
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        panOffset += dragAmount
                    }
                }
        ) {
            val canvasW = size.width
            val canvasH = size.height

            val padding = 40f
            val drawW = canvasW - padding * 2
            val drawH = canvasH - padding * 2

            val baseScale = minOf(drawW / rangeX, drawH / rangeY)
            val scale = baseScale * zoom

            val centerX = canvasW / 2f + panOffset.x
            val centerY = canvasH / 2f + panOffset.y
            val midX = (minX + maxX) / 2f
            val midY = (minY + maxY) / 2f

            val pixelSize = (scale / baseScale * 2f).coerceIn(2f, 8f)

            points.forEach { p ->
                val px = centerX + (p.x - midX) * scale
                val py = centerY + (p.y - midY) * scale

                drawCircle(
                    color = p.color,
                    radius = pixelSize,
                    center = Offset(px, py)
                )
            }
        }
    }
}

@Composable
private fun LegendDot(color: Color, label: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Canvas(modifier = Modifier.size(10.dp)) {
            drawCircle(color = color, radius = size.minDimension / 2f)
        }
        Text(label, color = Color.LightGray, style = MaterialTheme.typography.labelSmall)
    }
}
