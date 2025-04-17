package com.ahb.exrate.ui.components.base

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.ahb.exrate.ui.components.NoInternetScreen
import com.ahb.exrate.util.NetworkConnectivityObserver

@Composable
fun ConnectivityAwareContent(
    networkConnectivityObserver: NetworkConnectivityObserver,
    content: @Composable () -> Unit
) {
    val isConnected by networkConnectivityObserver.observe().collectAsState(initial = true)

    Box(modifier = Modifier.fillMaxSize()) {
        content()
        if (!isConnected) {
            NoInternetScreen(
                modifier = Modifier
                    .fillMaxSize()
            )
        }
    }
}