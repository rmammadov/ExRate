package com.ahb.exrate.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.ahb.exrate.ui.components.navigation.AppNavigation
import com.ahb.exrate.ui.theme.ExRateTheme
import com.ahb.exrate.util.NetworkConnectivityObserver
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var networkConnectivityObserver: NetworkConnectivityObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyComposeApp(networkConnectivityObserver)
        }
    }
}

@Composable
fun MyComposeApp(networkConnectivityObserver: NetworkConnectivityObserver) {
    ExRateTheme {
        val navController = rememberNavController()
        AppNavigation(
            navController = navController,
            networkConnectivityObserver = networkConnectivityObserver
        )
    }
}
