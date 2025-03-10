package com.ripplecode.reelmind.domain.model

import com.google.gson.annotations.SerializedName

data class MovieVideo(
    @SerializedName("id") val id: Int,
    @SerializedName("results") val results: List<MovieVideoResult>
)

data class MovieVideoResult(
    @SerializedName("name") val id: String,
    @SerializedName("key") val key: String,
    @SerializedName("site") val site: String,
    @SerializedName("type") val type: String
)
