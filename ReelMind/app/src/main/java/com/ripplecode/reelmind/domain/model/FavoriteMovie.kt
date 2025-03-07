package com.ripplecode.reelmind.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_movies")
data class FavoriteMovie(
    @PrimaryKey(autoGenerate = false) val id: Int,
    val title: String,
    val posterPath: String,
    val voteAverage: Double,
    val overview: String
)
