// TopAppBarWithPlus.kt
package com.ahb.exrate.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ahb.exrate.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarWithPlus(
    onAddClicked: () -> Unit = {}
) {
    val darkTextColor    = Color(0xFF111827)
    val lightCircleColor = Color(0xFFF2F2F2)

    TopAppBar(
        title = {
            Text(
                text  = stringResource(R.string.exchange_rates_title),
                style = MaterialTheme.typography.titleLarge,
                color = darkTextColor
            )
        },
        actions = {
            IconButton(onClick = onAddClicked) {
                Box(
                    modifier        = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(lightCircleColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector   = Icons.Default.Add,
                        contentDescription = stringResource(R.string.add_icon_description),
                        tint          = darkTextColor
                    )
                }
            }
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = Color.White
        )
    )
}
