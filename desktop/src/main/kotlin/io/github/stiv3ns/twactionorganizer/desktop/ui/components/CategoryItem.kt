package io.github.stiv3ns.twactionorganizer.desktop.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.stiv3ns.twactionorganizer.desktop.theme.TwColors

@Composable
fun CategoryItem(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    onRemove: (() -> Unit)? = null,
    indent: Int = 0
) {
    val bgColor = if (isSelected) {
        TwColors.tableHeader
    } else {
        TwColors.woodMedium
    }

    val textColor = if (isSelected) {
        TwColors.woodDark
    } else {
        TwColors.parchment
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = (12 + indent * 16).dp, end = 4.dp, top = 1.dp, bottom = 1.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(bgColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = textColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )

        if (onRemove != null) {
            IconButton(
                onClick = onRemove,
                modifier = Modifier.size(20.dp)
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Remove",
                    modifier = Modifier.size(14.dp),
                    tint = if (isSelected) TwColors.woodDark else TwColors.parchmentDark
                )
            }
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    onAdd: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = TwColors.goldBright,
            modifier = Modifier.weight(1f)
        )
        if (onAdd != null) {
            TextButton(
                onClick = onAdd,
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                modifier = Modifier.height(28.dp),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = TwColors.greenLight
                )
            ) {
                Text(
                    "+ Add",
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}
