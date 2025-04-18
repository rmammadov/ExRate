package com.ahb.exrate.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ahb.exrate.R
import com.ahb.exrate.ui.components.TopAppBarWithPlus
import com.ahb.exrate.ui.components.navigation.Routes
import com.ahb.exrate.ui.theme.BackgroundColor
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val screenState by homeViewModel.screenState.collectAsState()

    // tick every second for elapsedSeconds
    val now by produceState(initialValue = System.currentTimeMillis()) {
        while (true) {
            delay(1_000L)
            value = System.currentTimeMillis()
        }
    }
    val elapsedSeconds = ((now - screenState.lastUpdated) / 1000).coerceAtLeast(0)

    Scaffold(
        topBar = {
            TopAppBarWithPlus { navController.navigate(Routes.ADD_ASSET) }
        },
        containerColor = BackgroundColor
    ) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            // ← show linear bar when either initial load or pull‑to‑refresh is running
            if (screenState.isRefreshing) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                )
            }

            Text(
                text = stringResource(R.string.last_updated)
                    .plus(" $elapsedSeconds ")
                    .plus(stringResource(R.string.seconds_ago)),
                style    = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            HomeAssetList(
                items        = screenState.items,
                isRefreshing = screenState.isRefreshing,
                onRefresh    = { homeViewModel.onPullToRefreshTrigger() },
                onRemoveItem = { homeViewModel.onRemoveItem(it) }
            )
        }
    }
}
