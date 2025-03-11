package com.ripplecode.reelmind.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ripplecode.reelmind.data.repository.MovieRepository
import com.ripplecode.reelmind.data.store.UserPreferencesDataStore
import com.ripplecode.reelmind.domain.model.Movie
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MovieRouletteViewModel(
    private val repository: MovieRepository,
    private val userPreferencesDataStore: UserPreferencesDataStore
) : ViewModel() {

    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> = _movies

    private val _selectedMovie = MutableStateFlow<Movie?>(null)
    val selectedMovie: StateFlow<Movie?> = _selectedMovie

    init {
        loadMovies()
    }

    private fun loadMovies() {
        viewModelScope.launch {
            try {
                // Busca as recomendações com base nos gêneros favoritos do usuário
                userPreferencesDataStore.favoriteGenres.collect { genres ->
                    if (genres.isNotEmpty()) {
                        val genresId = genres.map { getGenreIdByName(it).toInt() }
                        val allMovies = repository.getMoviesForRoulette(genresId) // Busca os filmes conforme os critérios
                        _movies.value = allMovies.shuffled().take(5) // Sorteia 5 filmes
                    }
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Erro ao carregar filmes: ${e.message}")
            }

        }
    }

    fun shuffleMovies() {
        viewModelScope.launch {
            _movies.value = _movies.value.shuffled()
            delay(1000) // Pequeno delay para efeito visual
            _selectedMovie.value = _movies.value.random() // Escolhe um filme
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
