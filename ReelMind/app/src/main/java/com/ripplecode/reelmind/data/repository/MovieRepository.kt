package com.ripplecode.reelmind.data.repository

import com.ripplecode.reelmind.domain.model.Movie
import com.ripplecode.reelmind.data.remote.ApiService
import com.ripplecode.reelmind.domain.model.MovieDetail

class MovieRepository(private val apiService: ApiService) {
    suspend fun getPopularMovies(): List<Movie> {
        return apiService.getPopularMovies().results
    }

    suspend fun getMoviesByGenres(genreIds: String): List<Movie> {
        return apiService.getMoviesByGenres(genreIds).results
    }

    suspend fun getMovieDetail(movieId: Int): MovieDetail {
        return apiService.getMovieDetail(movieId)
    }

    suspend fun searchMovies(query: String): List<Movie> {
        return apiService.searchMovies(query).results
    }
}