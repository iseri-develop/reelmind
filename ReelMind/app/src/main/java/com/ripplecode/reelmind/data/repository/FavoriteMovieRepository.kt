package com.ripplecode.reelmind.data.repository

import com.ripplecode.reelmind.data.local.FavoriteMovieDao
import com.ripplecode.reelmind.domain.model.FavoriteMovie
import kotlinx.coroutines.flow.Flow

open class FavoriteMovieRepository(private val favoriteMovieDao: FavoriteMovieDao) {

    open fun getAllFavorites(): Flow<List<FavoriteMovie>> {
        return favoriteMovieDao.getAllFavorites()
    }

    suspend fun addToFavorites(movie: FavoriteMovie) {
        favoriteMovieDao.insertFavorite(movie)
    }

    suspend fun removeFromFavorites(movieId: Int) {
        favoriteMovieDao.deleteFavorite(movieId)
    }

    suspend fun isFavorite(movieId: Int): Boolean {
        return favoriteMovieDao.isFavorite(movieId)
    }
}
