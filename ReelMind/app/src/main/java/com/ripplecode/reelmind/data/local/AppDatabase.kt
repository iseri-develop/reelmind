package com.ripplecode.reelmind.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ripplecode.reelmind.domain.model.FavoriteMovie

@Database(entities = [FavoriteMovie::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteMovieDao(): FavoriteMovieDao
}
