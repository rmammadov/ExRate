package com.ahb.exrate.ui.components.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ahb.exrate.ui.components.base.ConnectivityAwareContent
import com.ahb.exrate.ui.screens.addasset.AddAssetScreen
import com.ahb.exrate.ui.screens.main.MainScreen
import com.ahb.exrate.ui.screens.splash.SplashScreen
import com.ahb.exrate.util.NetworkConnectivityObserver

@Composable
fun AppNavigation(
    navController: NavHostController,
    networkConnectivityObserver: NetworkConnectivityObserver,
    modifier: Modifier = Modifier
) {
    ConnectivityAwareContent(
        networkConnectivityObserver = networkConnectivityObserver
    ) {
        NavHost(
            navController = navController,
            startDestination = Routes.SPLASH,
            modifier = modifier
        ) {
            composable(Routes.SPLASH) {
                SplashScreen(navController)
            }
            composable(Routes.MAIN) {
                MainScreen(navController)
            }
            composable(Routes.ADD_ASSET) {
                AddAssetScreen(navController)
            }
        }
    }
}
