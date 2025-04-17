package com.ahb.exrate.ui.screens.addasset

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ahb.exrate.model.CurrencyItem
import com.ahb.exrate.ui.components.PullToRefreshWrapper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAssetScreen(
    navController: NavController,
    addAssetViewModel: AddAssetViewModel = hiltViewModel()
) {
    val fiatAssets    by addAssetViewModel.fiatAssets.collectAsState()
    val cryptoAssets  by addAssetViewModel.cryptoAssets.collectAsState()
    val searchQuery   by addAssetViewModel.searchQuery.collectAsState()
    val isRefreshing  by addAssetViewModel.isRefreshing.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Asset") },
                navigationIcon = {
                    IconButton(onClick = {
                        if (navController.previousBackStackEntry != null) {
                            navController.popBackStack()
                        }
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            addAssetViewModel.confirmSelection()
                            if (navController.previousBackStackEntry != null) {
                                navController.popBackStack()
                            }
                        },
                        enabled = searchQuery.isNotBlank() || fiatAssets.any { it.isSelected } || cryptoAssets.any { it.isSelected }
                    ) {
                        Text("Done")
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black,
                    navigationIconContentColor = Color.Black,
                    actionIconContentColor = Color.Black
                )
            )
        },
        containerColor = Color(0xFFF2F2F2)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            OutlinedTextField(
                value = TextFieldValue(searchQuery),
                onValueChange = { addAssetViewModel.onSearchQueryChanged(it.text) },
                placeholder = { Text("Search assets") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            Box(modifier = Modifier.fillMaxSize()) {
                PullToRefreshWrapper(
                    isRefreshing = isRefreshing,
                    onRefresh = { addAssetViewModel.onPullToRefreshTrigger() }
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            horizontal = 16.dp,
                            vertical = 16.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        fun CurrencyItem.matches() =
                            code.contains(searchQuery, ignoreCase = true) ||
                                    name.contains(searchQuery, ignoreCase = true)

                        val filteredFiat = fiatAssets.filter { it.matches() }
                        if (filteredFiat.isNotEmpty()) {
                            item {
                                Text(
                                    "POPULAR ASSETS",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.Gray,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 16.dp, bottom = 4.dp)
                                )
                            }
                            items(filteredFiat) { asset ->
                                AssetRow(
                                    asset = asset,
                                    onClick = { addAssetViewModel.toggleAsset(asset) }
                                )
                            }
                        }

                        val filteredCrypto = cryptoAssets.filter { it.matches() }
                        if (filteredCrypto.isNotEmpty()) {
                            item {
                                Text(
                                    "CRYPTOCURRENCIES",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.Gray,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 16.dp, top = 8.dp, bottom = 4.dp)
                                )
                            }
                            items(filteredCrypto) { asset ->
                                AssetRow(
                                    asset = asset,
                                    onClick = { addAssetViewModel.toggleAsset(asset) }
                                )
                            }
                        }
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
            .background(Color.White)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // icon circle
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(Color(0xFF333744)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = asset.code.firstOrNull()?.toString() ?: "?",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = asset.code,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
            Text(
                text = asset.name,
                color = Color.Gray,
                fontSize = 13.sp
            )
        }

        RadioButton(
            selected = asset.isSelected,
            onClick = onClick
        )
    }
}
