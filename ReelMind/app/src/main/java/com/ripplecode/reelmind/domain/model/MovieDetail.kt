package com.ripplecode.reelmind.domain.model

import com.google.gson.annotations.SerializedName

data class MovieDetail(
    val id: Int,
    val title: String,
    val overview: String,
    @SerializedName("poster_path") val posterPath: String,
    @SerializedName("backdrop_path") val backdropPath: String,
    @SerializedName("vote_average") val voteAverage: Double,
    val genres: List<Genre>,  // 🔄 Agora espera uma lista de objetos `Genre`
    val runtime: Int
)

data class Genre(
    val id: Int,
    val name: String  // 🔄 Adicionamos o campo correto que a API retorna
)
