package com.ripplecode.reelmind.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ripplecode.reelmind.data.repository.MovieRepository
import com.ripplecode.reelmind.domain.model.MovieDetail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailViewModel (private val repository: MovieRepository) : ViewModel() {
    private val _movieDetail = MutableStateFlow<MovieDetail?>(null)
    val movieDetail: StateFlow<MovieDetail?> = _movieDetail

    fun fetchMovieDetail(movieId: Int) {
        viewModelScope.launch {
            val movie = repository.getMovieDetail(movieId)
            val videos = repository.getMovieVideos(movieId)
            val providers = repository.getMovieProviders(movieId)

            val trailer = videos.firstOrNull { it.site == "YouTube" && it.type == "Trailer" }
            val trailerUrl = trailer?.key.let { "https://www.youtube.com/watch?v=$it" }

            _movieDetail.value = movie.copy(
                trailerUrl = trailerUrl,
                streamingProviders = providers
            )
        }
    }

    fun clearMovieDetail() {
        _movieDetail.value = null
    }
}