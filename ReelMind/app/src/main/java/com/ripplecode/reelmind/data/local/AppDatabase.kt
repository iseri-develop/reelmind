package com.ripplecode.reelmind.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ripplecode.reelmind.domain.model.FavoriteMovie
import com.ripplecode.reelmind.domain.model.WatchedMovie

@Database(entities = [FavoriteMovie::class, WatchedMovie::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteMovieDao(): FavoriteMovieDao
    abstract fun watchedMovieDao(): WatchedMovieDao
}
