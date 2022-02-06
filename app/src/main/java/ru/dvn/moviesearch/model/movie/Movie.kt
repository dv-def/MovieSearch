package ru.dvn.moviesearch.model.movie

data class Movie(
    val name: String,
    val year: String,
    val rating: Double,
    val isFavorite: Boolean = false
)