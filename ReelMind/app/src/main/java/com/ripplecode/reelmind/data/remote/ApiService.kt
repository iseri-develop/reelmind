package com.ripplecode.reelmind.data.remote

import com.ripplecode.reelmind.domain.model.MovieResponse
import com.ripplecode.reelmind.domain.model.MovieDetail
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("movie/popular")
    suspend fun getPopularMovies(): MovieResponse

    @GET("discover/movie")
    suspend fun getMoviesByGenres(
        @Query("with_genres") genreIds: String
    ): MovieResponse


    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") movieId: Int
    ): MovieDetail

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String
    ): MovieResponse
}