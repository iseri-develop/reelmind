package com.ripplecode.reelmind.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.ripplecode.reelmind.presentation.screens.LoginScreen
import com.ripplecode.reelmind.presentation.screens.OnboardingScreen
import com.ripplecode.reelmind.presentation.viewmodel.AuthViewModel
import org.koin.androidx.compose.get

@Composable
fun AppNavigation(
    userPreferencesDataStore: UserPreferencesDataStore = get(),
    authViewModel: AuthViewModel = get() // Obtém a ViewModel de autenticação
) {
    val navController = rememberNavController()
    val genres by userPreferencesDataStore.favoriteGenres.collectAsState(initial = emptySet())
//    val isUserLoggedIn by authViewModel.isUserLoggedIn.collectAsState(initial = false)

    // Verifica se precisa pular a Onboarding
    LaunchedEffect(genres) {
        if (genres.isNotEmpty()) {
            navController.navigate("home") {
                popUpTo("onboarding") { inclusive = true }
            }
        }
    }

    NavHost(navController = navController, startDestination = "onboarding") {
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
                onMovieClick = { movie -> selectedMovieId = movie.id },
//                onFavoritesClick = {
//                    if (isUserLoggedIn) {
//                        navController.navigate("favorites")
//                    } else {
//                        navController.navigate("login")
//                    }
//                },
//                onPlaylistsClick = {
//                    if (isUserLoggedIn) {
//                        navController.navigate("playlists")
//                    } else {
//                        navController.navigate("login")
//                    }
//                }
            )

            selectedMovieId?.let { movieId ->
                DetailScreen(movieId = movieId, onDismiss = { selectedMovieId = null })
            }
        }

//        composable("favorites") {
//            if (isUserLoggedIn) {
//                FavoritesScreen()
//            } else {
//                navController.navigate("login")
//            }
//        }
//
//        composable("playlists") {
//            if (isUserLoggedIn) {
//                PlaylistsScreen()
//            } else {
//                navController.navigate("login")
//            }
//        }

        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.popBackStack() // Volta para a tela anterior após login
                }
            )
        }
    }
}

