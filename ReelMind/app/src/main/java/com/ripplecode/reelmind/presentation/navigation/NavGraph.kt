package com.ripplecode.reelmind.presentation.navigation

import ProfileScreen
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ripplecode.reelmind.data.store.UserPreferencesDataStore
import com.ripplecode.reelmind.presentation.screens.DetailScreen
import com.ripplecode.reelmind.presentation.screens.HomeScreen
import com.ripplecode.reelmind.presentation.screens.OnboardingScreen
import com.ripplecode.reelmind.ui.components.BottomNavigationBar
import org.koin.androidx.compose.get

@Composable
fun AppNavigation(userPreferencesDataStore: UserPreferencesDataStore = get()) {
    val navController = rememberNavController()
    val genres by userPreferencesDataStore.favoriteGenres.collectAsState(initial = emptySet())

    val startDestination = if (genres.isEmpty()) "onboarding" else "home"

    Scaffold(
        bottomBar = {
            if (startDestination != "onboarding") {
                BottomNavigationBar(navController)
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(paddingValues)
        ) {
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
                    onMovieClick = { movie -> selectedMovieId = movie.id }
                )

                selectedMovieId?.let { movieId ->
                    DetailScreen(
                        movieId = movieId,
                        onDismiss = { selectedMovieId = null }
                    )
                }
            }

            composable("profile") {
                var selectedMovieId by remember { mutableStateOf<Int?>(null) }

                ProfileScreen(
                    navController = navController,
                    onMovieClick = { movie -> selectedMovieId = movie.id })

                selectedMovieId?.let { movieId ->
                    DetailScreen(
                        movieId = movieId,
                        onDismiss = { selectedMovieId = null }
                    )
                }
            }
        }
    }
}