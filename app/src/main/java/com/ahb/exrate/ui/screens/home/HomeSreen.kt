package com.ahb.exrate.ui.screens.home

import android.text.format.DateUtils
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

@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val screenState by homeViewModel.screenState.collectAsState()

    val now by produceState(initialValue = System.currentTimeMillis()) {
        while (true) {
            kotlinx.coroutines.delay(60_000L)
            value = System.currentTimeMillis()
        }
    }

    val relativeTime by remember(screenState.lastUpdated, now) {
        mutableStateOf(
            DateUtils.getRelativeTimeSpanString(
                screenState.lastUpdated,
                now,
                DateUtils.MINUTE_IN_MILLIS,
                DateUtils.FORMAT_ABBREV_RELATIVE
            ).toString()
        )
    }

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
                text = "Last updated: $relativeTime",
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
                        onRemoveItem = { /* TODO: wire up removal if desired */ }
                    )
                }
            }
        }
    }
}
