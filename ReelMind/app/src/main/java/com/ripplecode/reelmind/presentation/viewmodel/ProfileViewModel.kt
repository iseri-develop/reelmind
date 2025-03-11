package com.ripplecode.reelmind.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.ripplecode.reelmind.data.repository.FavoriteMovieRepository
import com.ripplecode.reelmind.data.repository.WatchedMovieRepository

class ProfileViewModel(
    favoriteRepository: FavoriteMovieRepository,
    watchedRepository: WatchedMovieRepository
) : ViewModel() {

    val favoriteMovies = favoriteRepository.getAllFavoritesListMovie()
    val watchedMovies = watchedRepository.getAllWatchedListMovie()
}
