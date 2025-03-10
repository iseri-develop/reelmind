package com.ripplecode.reelmind.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ripplecode.reelmind.data.repository.WatchedMovieRepository
import com.ripplecode.reelmind.domain.model.WatchedMovie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class WatchedMovieViewModel(private val repository: WatchedMovieRepository) : ViewModel() {
    val watchedMovies = repository.watchedMovies

    fun isMovieWatched(movieId: Int): Flow<Boolean> {
        return repository.isMovieWatched(movieId)
    }

    fun toggleWatched(movie: WatchedMovie) {
        viewModelScope.launch {
            val isWatched = repository.isMovieWatched(movie.id).first()
            if (isWatched) {
                repository.removeFromWatched(movie)
            } else {
                repository.addToWatched(movie)
            }
        }
    }
}
