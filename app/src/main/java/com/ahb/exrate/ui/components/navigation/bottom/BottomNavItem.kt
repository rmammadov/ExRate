package com.ahb.exrate.ui.components.navigation.bottom

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.ahb.exrate.R

sealed class BottomNavItem(
    val route: String,
    @StringRes val labelRes: Int,
    @DrawableRes val icon: Int
) {
    object Home : BottomNavItem(
        route = "Home",
        labelRes = R.string.bottom_nav_home,
        icon = R.drawable.ic_home
    )

    object Favorites : BottomNavItem(
        route = "Favorites",
        labelRes = R.string.bottom_nav_favorites,
        icon = R.drawable.ic_favorite
    )

    object Markets : BottomNavItem(
        route = "Markets",
        labelRes = R.string.bottom_nav_markets,
        icon = R.drawable.ic_market
    )

    object Settings : BottomNavItem(
        route = "Settings",
        labelRes = R.string.bottom_nav_settings,
        icon = R.drawable.ic_settings
    )
}