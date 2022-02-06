package ru.dvn.moviesearch.model

import java.util.*

data class Movie(
    val icon: Int,
    val name: String,
    val year: String,
    val rating: Double,
    val date: Date,
    val isFavorite: Boolean
)