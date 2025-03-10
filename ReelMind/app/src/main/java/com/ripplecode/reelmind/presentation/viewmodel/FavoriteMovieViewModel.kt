package com.ripplecode.reelmind.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ripplecode.reelmind.data.repository.FavoriteMovieRepository
import com.ripplecode.reelmind.domain.model.FavoriteMovie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoriteMovieViewModel(private val repository: FavoriteMovieRepository) : ViewModel() {

    private val _favoriteMovies = repository.getAllFavorites()

    fun isFavorite(movieId: Int): Flow<Boolean> {
        return repository.isFavorite(movieId)
    }

    fun toggleFavorite(movie: FavoriteMovie) {
        viewModelScope.launch {
            if (isFavorite(movie.id).first()) {
                repository.removeFromFavorites(movie.id)
            } else {
                repository.addToFavorites(movie)
            }
        }
    }
}
