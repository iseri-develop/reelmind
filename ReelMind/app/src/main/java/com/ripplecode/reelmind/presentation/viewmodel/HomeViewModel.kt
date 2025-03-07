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

    private val _trendingMovies = MutableStateFlow<List<Movie>>(emptyList()) // 🔥 Em Alta
    val trendingMovies: StateFlow<List<Movie>> = _trendingMovies

    private val _topRatedMovies = MutableStateFlow<List<Movie>>(emptyList()) // ⭐ Melhores Avaliados
    val topRatedMovies: StateFlow<List<Movie>> = _topRatedMovies

    private val _latestMovies = MutableStateFlow<List<Movie>>(emptyList()) // 🎞️ Lançamentos
    val latestMovies: StateFlow<List<Movie>> = _latestMovies

    private val _recommendedMovies = MutableStateFlow<List<Movie>>(emptyList()) // 🎬 Recomendações
    val recommendedMovies: StateFlow<List<Movie>> = _recommendedMovies

    private val _searchResults = MutableStateFlow<List<Movie>>(emptyList()) // 🔍 Busca
    val searchResults: StateFlow<List<Movie>> = _searchResults

    init {
        fetchMovies()
    }

    private fun fetchMovies() {
        viewModelScope.launch {
            try {
                _trendingMovies.value = repository.getTrendingMovies()
                _topRatedMovies.value = repository.getTopRatedMovies()
                _latestMovies.value = repository.getLatestMovies()

                // Busca as recomendações com base nos gêneros favoritos do usuário
                userPreferencesDataStore.favoriteGenres.collect { genres ->
                    if (genres.isNotEmpty()) {
                        val genreIds = genres.joinToString(",") { getGenreIdByName(it) }
                        _recommendedMovies.value = repository.getMoviesByGenres(genreIds)
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

