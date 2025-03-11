package com.ripplecode.reelmind.data.repository

import com.ripplecode.reelmind.data.local.FavoriteMovieDao
import com.ripplecode.reelmind.domain.model.FavoriteMovie
import com.ripplecode.reelmind.domain.model.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

open class FavoriteMovieRepository(private val favoriteMovieDao: FavoriteMovieDao) {

    fun getAllFavorites(): Flow<List<FavoriteMovie>> {
        return favoriteMovieDao.getAllFavorites()
    }

    fun getAllFavoritesListMovie() :Flow<List<Movie>> {
        return favoriteMovieDao.getAllFavorites().map { favoriteMovies ->
            favoriteMovies.map { favoriteMovie ->
                Movie(
                    id = favoriteMovie.id,
                    title = favoriteMovie.title,
                    overview = favoriteMovie.overview,
                    posterPath = favoriteMovie.posterPath,
                    voteAverage = favoriteMovie.voteAverage.toFloat(),
                    genreIds = emptyList()
                )
            }
        }
    }

    suspend fun addToFavorites(movie: FavoriteMovie) {
        favoriteMovieDao.insertFavorite(movie)
    }

    suspend fun removeFromFavorites(movieId: Int) {
        favoriteMovieDao.deleteFavorite(movieId)
    }

    fun isFavorite(movieId: Int): Flow<Boolean> = favoriteMovieDao.isFavorite(movieId)
}
