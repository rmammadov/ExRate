// CurrencyList.kt
package com.ahb.exrate.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.rememberDismissState
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ahb.exrate.R
import com.ahb.exrate.model.CurrencyItem

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CurrencyList(
    items: List<CurrencyItem>,
    onRemoveItem: (CurrencyItem) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items, key = { it.code }) { currency ->
            val dismissState = rememberDismissState(
                confirmStateChange = { dismissValue ->
                    if (dismissValue == DismissValue.DismissedToEnd ||
                        dismissValue == DismissValue.DismissedToStart
                    ) {
                        onRemoveItem(currency)
                    }
                    true
                }
            )

            SwipeToDismiss(
                state = dismissState,
                directions = setOf(
                    DismissDirection.EndToStart,
                    DismissDirection.StartToEnd
                ),
                background = {
                    val direction = dismissState.dismissDirection
                    val alignment = if (direction == DismissDirection.StartToEnd) {
                        Alignment.CenterStart
                    } else {
                        Alignment.CenterEnd
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF424957)),
                        contentAlignment = alignment
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(R.string.remove_currency),
                            tint = Color.White,
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )
                    }
                },
                dismissContent = {
                    CurrencyListItem(currency = currency)
                }
            )
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(R.string.swipe_to_remove_hint),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                textAlign = TextAlign.Center,
                color = Color.Gray,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun CurrencyListItem(currency: CurrencyItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(50))
                .background(Color(0xFF333744)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = currency.code.firstOrNull()?.toString() ?: "?",
                color = Color.White,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )
        }

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = currency.code,
                fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
                fontSize = 16.sp
            )
            Text(
                text = currency.name,
                fontSize = 13.sp,
                color = Color.Gray
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = currency.rate,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = currency.change,
                color = if (currency.change.startsWith("+")) Color(0xFF4CAF50) else Color(0xFFF44336),
                fontSize = 13.sp
            )
        }
    }
}
