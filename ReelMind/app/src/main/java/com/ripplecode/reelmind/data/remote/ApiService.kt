package com.ripplecode.reelmind.data.remote

import com.ripplecode.reelmind.domain.model.MovieResponse
import com.ripplecode.reelmind.domain.model.MovieDetail
import com.ripplecode.reelmind.domain.model.MovieProvidersResponse
import com.ripplecode.reelmind.domain.model.MovieVideo
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("movie/popular")
    suspend fun getPopularMovies(): MovieResponse

    @GET("trending/movie/day") // 🔥 Em Alta
    suspend fun getTrendingMovies(): MovieResponse

    @GET("discover/movie")
    suspend fun getTrendingMoviesByGenres(
        @Query("with_genres") genreIds: String,
        @Query("sort_by") sortBy: String = "popularity.desc"
    ): MovieResponse

    @GET("movie/top_rated") // ⭐ Melhores Avaliados
    suspend fun getTopRatedMovies(): MovieResponse

    @GET("discover/movie")
    suspend fun getTopRatedMoviesByGenres(
        @Query("with_genres") genreIds: String,
        @Query("sort_by") sortBy: String = "vote_average.desc",
        @Query("vote_count.gte") voteCount: Int = 1000 // Evita filmes com poucas avaliações
    ): MovieResponse


    @GET("movie/now_playing") // 🎞️ Lançamentos
    suspend fun getLatestMovies(): MovieResponse

    @GET("discover/movie")
    suspend fun getNowPlayingMoviesByGenres(
        @Query("with_genres") genreIds: String,
        @Query("primary_release_date.gte") minDate: String,
        @Query("primary_release_date.lte") maxDate: String
    ): MovieResponse

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

    @GET("movie/{movie_id}/videos") // 🎬 Videos do filme
    suspend fun getMovieVideos(
        @Path("movie_id") movieId: Int
    ): MovieVideo

    // 🎬 Plataformas de streaming
    @GET("movie/{movie_id}/watch/providers")
    suspend fun getMovieProviders(
        @Path("movie_id") movieId: Int
    ): MovieProvidersResponse
}
