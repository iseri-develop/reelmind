package com.ripplecode.reelmind.data.repository

import com.ripplecode.reelmind.data.remote.ApiService
import com.ripplecode.reelmind.domain.model.Movie
import com.ripplecode.reelmind.domain.model.MovieDetail
import com.ripplecode.reelmind.domain.model.MovieVideoResult
import com.ripplecode.reelmind.domain.model.StreamingPlatform
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first

class MovieRepository(
    private val apiService: ApiService,
    private val favoriteRepository: FavoriteMovieRepository,
    private val watchedRepository: WatchedMovieRepository
) {

    suspend fun getPopularMovies(): List<Movie> {
        return apiService.getPopularMovies().results
    }

    suspend fun getTrendingMovies(): List<Movie> { // 🔥 Em Alta
        return apiService.getTrendingMovies().results
    }

    suspend fun getTrendingMoviesByGenres(genres: List<Int>): List<Movie> { // 🔥 Em Alta
        val genreIds = genres.joinToString(",")
        return apiService.getTrendingMoviesByGenres(genreIds).results
    }

    suspend fun getTopRatedMovies(): List<Movie> { // ⭐ Melhores Avaliados
        return apiService.getTopRatedMovies().results
    }

    suspend fun getTopRatedMoviesByGenres(genres: List<Int>): List<Movie> { // ⭐ Melhores Avaliados
        val genreIds = genres.joinToString(",")
        return apiService.getTopRatedMoviesByGenres(genreIds).results
    }

    suspend fun getLatestMovies(): List<Movie> { // 🎞️ Lançamentos
        return apiService.getLatestMovies().results
    }

    suspend fun getNowPlayingMoviesByGenres(
        genres: List<Int>,
        minDate: String,
        maxDate: String
    ): List<Movie> { // 🎞️ Lançamentos
        val genreIds = genres.joinToString(",")
        return apiService.getNowPlayingMoviesByGenres(genreIds, minDate, maxDate).results
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

    suspend fun getMoviesForRoulette(selectedGenres: List<Int>): List<Movie> {
//        val favoriteMovies = favoriteRepository.getAllFavoritesListMovie().first()
//        val watchedMovies = watchedRepository.getAllWatchedListMovie().first()
//
//        val combinedMovies = (favoriteMovies + watchedMovies).distinct()
//
//        return if (combinedMovies.size >= 5) {
//            combinedMovies.shuffled().take(5) // Pega 5 aleatórios se houver suficientes
//        } else {
//            val recommendedMovies = apiService.getMoviesByGenres(selectedGenres.joinToString(",")).results
//            (combinedMovies + recommendedMovies).distinct().shuffled().take(5)
//        }

        return apiService.getMoviesByGenres(selectedGenres.joinToString(",")).results.shuffled()
            .take(5)
    }

    suspend fun getMovieVideos(movieId: Int): List<MovieVideoResult> {
        return apiService.getMovieVideos(movieId).results
    }

    suspend fun getMovieProviders(movieId: Int): List<StreamingPlatform> {
        return try {
            val response = apiService.getMovieProviders(movieId)
            val providers = response.results["BR"]?.flatrate ?: emptyList()
            providers.map { provider ->
                StreamingPlatform(
                    name = provider.name,
                    logoUrl = "https://image.tmdb.org/t/p/w500${provider.logoPath}"
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
