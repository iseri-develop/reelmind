package com.ripplecode.reelmind.data.repository

import com.ripplecode.reelmind.data.local.WatchedMovieDao
import com.ripplecode.reelmind.domain.model.Movie
import com.ripplecode.reelmind.domain.model.WatchedMovie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WatchedMovieRepository(private val dao: WatchedMovieDao) {
    val watchedMovies: Flow<List<WatchedMovie>> = dao.getAllWatchedMovies()

    fun isMovieWatched(movieId: Int): Flow<Boolean> = dao.isMovieWatched(movieId)

    suspend fun addToWatched(movie: WatchedMovie) {
        dao.insert(movie)
    }

    suspend fun removeFromWatched(movie: WatchedMovie) {
        dao.delete(movie)
    }

    fun getAllWatched(): Flow<List<WatchedMovie>> {
        return watchedMovies
    }

    fun getAllWatchedListMovie() :Flow<List<Movie>> {
        return watchedMovies.map { watchedMovies ->
            watchedMovies.map { watchedMovie ->
                Movie(
                    id = watchedMovie.id,
                    title = watchedMovie.title,
                    overview = watchedMovie.overview,
                    posterPath = watchedMovie.posterPath,
                    voteAverage = watchedMovie.voteAverage.toFloat(),
                    genreIds = emptyList()
                )
            }
        }
    }
}
