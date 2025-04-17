package com.ahb.exrate.ui.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ahb.exrate.R
import com.ahb.exrate.ui.components.navigation.Routes
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController,
    splashViewModel: SplashViewModel = hiltViewModel()
) {
    // Duration of the splash screen in milliseconds
    val splashDuration = 2000L

    // Start a coroutine to delay navigation
    LaunchedEffect(key1 = true) {
        delay(splashDuration)
        navController.navigate(Routes.MAIN) {
            popUpTo(Routes.SPLASH) { inclusive = true }
        }
    }

    // Splash screen UI
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray),  // You can change the background color if needed
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // "Logo" text with USD sign. Adjust font size and style as needed.
            Text(
                text = "$",  // USD sign acting as the logo
                fontSize = 96.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White  // Customize color as desired
            )
            Spacer(modifier = Modifier.height(16.dp))
            // App name text below the logo text
            Text(
                text = "EXRATE",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White  // Customize color if needed
            )
        }
    }
}