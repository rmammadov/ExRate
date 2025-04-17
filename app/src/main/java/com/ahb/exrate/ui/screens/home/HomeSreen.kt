package com.ahb.exrate.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ahb.exrate.ui.components.*
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val screenState by homeViewModel.screenState.collectAsState()

    // Tick every second so we can recompute elapsed time
    val now by produceState(initialValue = System.currentTimeMillis()) {
        while (true) {
            delay(1_000L)
            value = System.currentTimeMillis()
        }
    }

    // Compute how many seconds have passed since last update
    val elapsedSeconds = ((now - screenState.lastUpdated) / 1000).coerceAtLeast(0)

    Scaffold(
        topBar = {
            TopAppBarWithPlus { navController.navigate("add_asset") }
        },
        containerColor = Color(0xFFF2F2F2)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Last updated: $elapsedSeconds seconds ago",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Box(modifier = Modifier.fillMaxSize()) {
                PullToRefreshWrapper(
                    isRefreshing = screenState.isRefreshing,
                    onRefresh    = { homeViewModel.onPullToRefreshTrigger() }
                ) {
                    CurrencyList(
                        items        = screenState.items,
                        onRemoveItem = { homeViewModel.onRemoveItem(it) }
                    )
                }
            }
        }
    }
}
