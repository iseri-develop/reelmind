package com.ripplecode.reelmind.di

import android.content.Context
import androidx.room.Room
import com.ripplecode.reelmind.data.local.AppDatabase
import com.ripplecode.reelmind.data.local.FavoriteMovieDao
import com.ripplecode.reelmind.data.local.WatchedMovieDao
import org.koin.dsl.module

val databaseModule = module {
    single { provideDatabase(get()) }
    single { provideFavoriteMovieDao(get()) }
    single { provideWatchedMovieDao(get()) }
}

fun provideDatabase(context: Context): AppDatabase {
    return Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "reelmind_database"
    ).fallbackToDestructiveMigration()
        .build()
}

fun provideFavoriteMovieDao(database: AppDatabase): FavoriteMovieDao {
    return database.favoriteMovieDao()
}

fun provideWatchedMovieDao(database: AppDatabase): WatchedMovieDao {
    return database.watchedMovieDao()
}