package com.ripplecode.reelmind.data.repository

import com.ripplecode.reelmind.domain.model.Movie
import com.ripplecode.reelmind.data.remote.ApiService
import com.ripplecode.reelmind.domain.model.MovieDetail

class MovieRepository(private val apiService: ApiService) {

    suspend fun getPopularMovies(): List<Movie> {
        return apiService.getPopularMovies().results
    }

    suspend fun getTrendingMovies(): List<Movie> { // 🔥 Em Alta
        return apiService.getTrendingMovies().results
    }

    suspend fun getTopRatedMovies(): List<Movie> { // ⭐ Melhores Avaliados
        return apiService.getTopRatedMovies().results
    }

    suspend fun getLatestMovies(): List<Movie> { // 🎞️ Lançamentos
        return apiService.getLatestMovies().results
    }

    suspend fun getMoviesByGenres(genreIds: String): List<Movie> { // 🎬 Recomendações
        return apiService.getMoviesByGenres(genreIds).results
    }

    suspend fun getMovieDetail(movieId: Int): MovieDetail {
        return apiService.getMovieDetail(movieId)
    }

    suspend fun searchMovies(query: String): List<Movie> { // 🔍 Busca
        return apiService.searchMovies(query).results
    }
}
