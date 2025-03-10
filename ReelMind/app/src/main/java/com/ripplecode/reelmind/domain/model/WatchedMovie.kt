package com.ripplecode.reelmind.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watched_movies")
data class WatchedMovie(
    @PrimaryKey val id:Int,
    val title: String,
    val posterPath: String,
    val voteAverage: Double,
    val overview: String
)
