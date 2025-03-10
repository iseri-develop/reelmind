package com.ripplecode.reelmind.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ripplecode.reelmind.domain.model.WatchedMovie
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchedMovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: WatchedMovie)

    @Delete
    suspend fun delete(movie: WatchedMovie)

    @Query("SELECT * FROM watched_movies")
    fun getAllWatchedMovies(): Flow<List<WatchedMovie>>

    @Query("SELECT EXISTS (SELECT 1 FROM watched_movies WHERE id = :movieId)")
    fun isMovieWatched(movieId: Int): Flow<Boolean>
}
