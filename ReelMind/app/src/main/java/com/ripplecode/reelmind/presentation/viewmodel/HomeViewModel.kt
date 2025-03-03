package com.ripplecode.reelmind.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ripplecode.reelmind.data.repository.MovieRepository
import com.ripplecode.reelmind.data.store.UserPreferencesDataStore
import com.ripplecode.reelmind.domain.model.Movie
import com.ripplecode.reelmind.domain.model.MovieDetail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: MovieRepository,
    private val userPreferencesDataStore: UserPreferencesDataStore
) : ViewModel() {

    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> = _movies

    private val _searchResults = MutableStateFlow<List<Movie>>(emptyList())
    val searchResults: StateFlow<List<Movie>> = _searchResults

    init {
        fetchMoviesByUserPreferences()
    }

    private fun fetchMoviesByUserPreferences() {
        viewModelScope.launch {
            try {
                userPreferencesDataStore.favoriteGenres.collect { genres ->
                    if (genres.isEmpty()) {
                        _movies.value = repository.getPopularMovies()
                    } else {
                        val genreIds = genres.joinToString(",") { getGenreIdByName(it) }
                        _movies.value = repository.getMoviesByGenres(genreIds)
                    }
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Erro ao carregar filmes: ${e.message}")
            }
        }
    }

    fun searchMovies(query: String) {
        viewModelScope.launch {
            _searchResults.value = repository.searchMovies(query)
        }
    }

    private fun getGenreIdByName(genreName: String): String {
        val genreMap = mapOf(
            "Ação" to "28",
            "Comédia" to "35",
            "Drama" to "18",
            "Terror" to "27",
            "Ficção Científica" to "878"
        )
        return genreMap[genreName] ?: ""
    }
}
