package com.ripplecode.reelmind.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ripplecode.reelmind.presentation.screens.DetailScreen
import com.ripplecode.reelmind.presentation.screens.OnboardingScreen
import com.ripplecode.reelmind.presentation.screens.HomeScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "onboarding") {
        composable("onboarding") { OnboardingScreen(navController) }
        composable("home") {
            var selectedMovieId by remember { mutableStateOf<Int?>(null) }

            HomeScreen { movie ->
                selectedMovieId = movie.id
            }

            selectedMovieId?.let { movieId ->
                DetailScreen(
                    movieId = movieId,
                    onDismiss = { selectedMovieId = null }
                )
            }
        }
    }
}