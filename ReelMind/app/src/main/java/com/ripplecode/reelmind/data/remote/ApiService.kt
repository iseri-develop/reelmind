package com.ripplecode.reelmind.data.remote

import com.ripplecode.reelmind.domain.model.MovieResponse
import com.ripplecode.reelmind.domain.model.MovieDetail
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("movie/popular")
    suspend fun getPopularMovies(): MovieResponse

    @GET("trending/movie/day") // 🔥 Em Alta
    suspend fun getTrendingMovies(): MovieResponse

    @GET("movie/top_rated") // ⭐ Melhores Avaliados
    suspend fun getTopRatedMovies(): MovieResponse

    @GET("movie/now_playing") // 🎞️ Lançamentos
    suspend fun getLatestMovies(): MovieResponse

    @GET("discover/movie") // 🎬 Recomendações por gênero
    suspend fun getMoviesByGenres(
        @Query("with_genres") genreIds: String
    ): MovieResponse

    @GET("movie/{movie_id}") // 📄 Detalhes do filme
    suspend fun getMovieDetail(
        @Path("movie_id") movieId: Int
    ): MovieDetail

    @GET("search/movie") // 🔍 Busca de filmes
    suspend fun searchMovies(
        @Query("query") query: String
    ): MovieResponse
}
