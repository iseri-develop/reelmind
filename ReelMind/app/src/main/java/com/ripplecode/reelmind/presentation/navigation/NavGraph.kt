package com.ripplecode.reelmind.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ripplecode.reelmind.data.store.UserPreferencesDataStore
import com.ripplecode.reelmind.presentation.screens.DetailScreen
import com.ripplecode.reelmind.presentation.screens.HomeScreen
import com.ripplecode.reelmind.presentation.screens.OnboardingScreen
import org.koin.androidx.compose.get

@Composable
fun AppNavigation(userPreferencesDataStore: UserPreferencesDataStore = get()) {
    val navController = rememberNavController()
    val genres by userPreferencesDataStore.favoriteGenres.collectAsState(initial = emptySet())

    val startDestination = if (genres.isEmpty()) "onboarding" else "home"

    NavHost(navController = navController, startDestination = startDestination) {
        composable("onboarding") {
            OnboardingScreen(
                navController = navController,
                onFinish = {
                    navController.navigate("home") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                }
            )
        }

        composable("home") {
            var selectedMovieId by remember { mutableStateOf<Int?>(null) }

            HomeScreen(
                genres = genres,
                onMovieClick = { movie -> selectedMovieId = movie.id }
            )

            selectedMovieId?.let { movieId ->
                DetailScreen(
                    movieId = movieId,
                    onDismiss = { selectedMovieId = null }
                )
            }
        }
    }
}


