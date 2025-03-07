package com.ripplecode.reelmind.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ripplecode.reelmind.data.repository.FavoriteMovieRepository
import com.ripplecode.reelmind.domain.model.FavoriteMovie
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoriteMovieViewModel(private val repository: FavoriteMovieRepository) : ViewModel() {

    private val _favoriteMovies = repository.getAllFavorites()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favoriteMovies: StateFlow<List<FavoriteMovie>> = _favoriteMovies

    fun addToFavorites(movie: FavoriteMovie) {
        viewModelScope.launch {
            repository.addToFavorites(movie)
        }
    }

    fun removeFromFavorites(movieId: Int) {
        viewModelScope.launch {
            repository.removeFromFavorites(movieId)
        }
    }

    fun isFavorite(movieId: Int): StateFlow<Boolean> {
        return _favoriteMovies.map { list -> list.any { it.id == movieId } }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)
    }
}
