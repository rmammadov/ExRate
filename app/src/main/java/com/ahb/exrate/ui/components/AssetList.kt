package com.ahb.exrate.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ahb.exrate.R
import com.ahb.exrate.model.CurrencyItem
import com.ahb.exrate.ui.theme.BackgroundColorDarker
import com.ahb.exrate.ui.theme.ColorGray
import com.ahb.exrate.ui.theme.ColorWhite

@Composable
fun AssetList(
    fiatAssets: List<CurrencyItem>,
    cryptoAssets: List<CurrencyItem>,
    searchQuery: String,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onToggle: (CurrencyItem) -> Unit
) {
    // helper to filter by code/name
    fun CurrencyItem.matches() =
        code.contains(searchQuery, ignoreCase = true) ||
                name.contains(searchQuery, ignoreCase = true)

    Box(modifier = Modifier.fillMaxSize()) {
        PullToRefreshWrapper(
            isRefreshing = isRefreshing,
            onRefresh    = onRefresh
        ) {
            LazyColumn(
                modifier          = Modifier.fillMaxSize(),
                contentPadding    = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val filteredFiat = fiatAssets.filter { it.matches() }
                if (filteredFiat.isNotEmpty()) {
                    item { SectionHeader(titleRes = R.string.popular_assets) }
                    items(filteredFiat) { asset ->
                        AssetRow(asset = asset, onClick = { onToggle(asset) })
                    }
                }

                val filteredCrypto = cryptoAssets.filter { it.matches() }
                if (filteredCrypto.isNotEmpty()) {
                    item { SectionHeader(titleRes = R.string.cryptocurrencies) }
                    items(filteredCrypto) { asset ->
                        AssetRow(asset = asset, onClick = { onToggle(asset) })
                    }
                }
            }
        }
    }
}

@Composable
private fun AssetRow(
    asset: CurrencyItem,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(ColorWhite)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // icon circle
        Box(
            modifier        = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(BackgroundColorDarker),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text       = asset.code.firstOrNull()?.toString() ?: "?",
                color      = ColorWhite,
                fontWeight = FontWeight.Bold,
                fontSize   = 16.sp
            )
        }

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text       = asset.code,
                fontWeight = FontWeight.SemiBold,
                fontSize   = 16.sp
            )
            Text(
                text     = asset.name,
                color    = ColorGray,
                fontSize = 13.sp
            )
        }

        RadioButton(
            selected = asset.isSelected,
            onClick  = onClick
        )
    }
}

