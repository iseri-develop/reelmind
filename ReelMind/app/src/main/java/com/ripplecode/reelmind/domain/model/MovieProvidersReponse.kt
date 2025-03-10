package com.ripplecode.reelmind.domain.model

import com.google.gson.annotations.SerializedName

data class MovieProvidersResponse(
    val id: Int,
    val results: Map<String, CountryProviders>
)

data class CountryProviders(
    @SerializedName("flatrate") val flatrate: List<StreamingProvider>?
)

data class StreamingProvider(
    @SerializedName("provider_id") val id: Int,
    @SerializedName("provider_name") val name: String,
    @SerializedName("logo_path") val logoPath: String?
)

data class StreamingPlatform(
    val name: String,
    val logoUrl: String
)
