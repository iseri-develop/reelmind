package com.ripplecode.reelmind.data.repository

import com.ripplecode.reelmind.data.local.FavoriteMovieDao
import com.ripplecode.reelmind.domain.model.FavoriteMovie
import kotlinx.coroutines.flow.Flow

open class FavoriteMovieRepository(private val favoriteMovieDao: FavoriteMovieDao) {

    fun getAllFavorites(): Flow<List<FavoriteMovie>> {
        return favoriteMovieDao.getAllFavorites()
    }

    suspend fun addToFavorites(movie: FavoriteMovie) {
        favoriteMovieDao.insertFavorite(movie)
    }

    suspend fun removeFromFavorites(movieId: Int) {
        favoriteMovieDao.deleteFavorite(movieId)
    }

    fun isFavorite(movieId: Int): Flow<Boolean> = favoriteMovieDao.isFavorite(movieId)
}
