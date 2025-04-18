package com.ahb.exrate.ui.screens.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ahb.exrate.R
import com.ahb.exrate.ui.components.navigation.bottom.BottomNavItem
import com.ahb.exrate.ui.components.navigation.bottom.BottomNavigationBar
import com.ahb.exrate.ui.components.navigation.bottom.bottomNavItems
import com.ahb.exrate.ui.screens.favorites.FavoritesScreen
import com.ahb.exrate.ui.screens.home.HomeScreen
import com.ahb.exrate.ui.screens.markets.MarketsScreen
import com.ahb.exrate.ui.screens.settings.SettingsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val bottomNavItemsList = bottomNavItems
    var selectedItem by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                items = bottomNavItemsList,
                currentIndex = selectedItem,
                onItemSelected = { index ->
                    selectedItem = index
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            when (val current = bottomNavItemsList.getOrNull(selectedItem)) {
                is BottomNavItem.Home -> {
                    HomeScreen(
                        navController = navController
                    )
                }
                is BottomNavItem.Favorites -> {
                    FavoritesScreen(
                        navController = navController
                    )
                }
                is BottomNavItem.Markets -> {
                    MarketsScreen(
                        navController = navController
                    )
                }
                is BottomNavItem.Settings -> {
                    SettingsScreen(
                        navController = navController
                    )
                }
                else -> {
                    Text(stringResource(R.string.invalid_selection), modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}