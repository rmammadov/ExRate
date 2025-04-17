package com.ahb.exrate.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarWithPlus(
    onAddClicked: () -> Unit = {}
) {
    val darkTextColor = Color(0xFF111827)  // near-black
    val lightCircleColor = Color(0xFFF2F2F2) // light gray for the circle background

    TopAppBar(
        title = {
            androidx.compose.material.Text(
                text = "Exchange Rates",
                style = MaterialTheme.typography.titleLarge,
                color = darkTextColor
            )
        },
        actions = {
            androidx.compose.material.IconButton(onClick = onAddClicked) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(lightCircleColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        tint = darkTextColor
                    )
                }
            }
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = Color.White
        )
    )
}
