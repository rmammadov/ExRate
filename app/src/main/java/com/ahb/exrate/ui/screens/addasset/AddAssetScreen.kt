package com.ahb.exrate.ui.screens.addasset

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ahb.exrate.R
import com.ahb.exrate.ui.components.AssetList
import com.ahb.exrate.ui.theme.BackgroundColor
import com.ahb.exrate.ui.theme.ColorBlack
import com.ahb.exrate.ui.theme.ColorWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAssetScreen(
    navController: NavController,
    addAssetViewModel: AddAssetViewModel = hiltViewModel()
) {
    val fiatAssets   by addAssetViewModel.fiatAssets.collectAsState()
    val cryptoAssets by addAssetViewModel.cryptoAssets.collectAsState()
    val searchQuery  by addAssetViewModel.searchQuery.collectAsState()
    val isRefreshing by addAssetViewModel.isRefreshing.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.add_asset_title)) },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.previousBackStackEntry?.let { navController.popBackStack() }
                    }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            navController.previousBackStackEntry?.let { navController.popBackStack() }
                        },
                        enabled = searchQuery.isNotBlank()
                                || fiatAssets.any { it.isSelected }
                                || cryptoAssets.any { it.isSelected }
                    ) {
                        Text(stringResource(R.string.done))
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor             = ColorWhite,
                    titleContentColor          = ColorBlack,
                    navigationIconContentColor = ColorBlack,
                    actionIconContentColor     = ColorBlack
                )
            )
        },
        containerColor = BackgroundColor
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // ← Linear loading indicator under the TopAppBar
            if (isRefreshing) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                )
            }

            OutlinedTextField(
                value         = TextFieldValue(searchQuery),
                onValueChange = { addAssetViewModel.onSearchQueryChanged(it.text) },
                placeholder   = { Text(stringResource(R.string.search_assets)) },
                singleLine    = true,
                modifier      = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            AssetList(
                fiatAssets   = fiatAssets,
                cryptoAssets = cryptoAssets,
                searchQuery  = searchQuery,
                isRefreshing = isRefreshing,
                onRefresh    = { addAssetViewModel.onPullToRefreshTrigger() },
                onToggle     = { addAssetViewModel.toggleAsset(it) }
            )
        }
    }
}
