package com.ripplecode.reelmind.domain.model

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @SerializedName("results") val results: List<Movie>
)

data class Movie(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("vote_average") val voteAverage: Float,
    @SerializedName("overview") val overview: String,
    @SerializedName("genre_ids") val genreIds: List<Int>
) {
    companion object {
        fun empty(): Movie {
            return Movie(0, "", null, 0f, "", emptyList())
        }
    }
}
