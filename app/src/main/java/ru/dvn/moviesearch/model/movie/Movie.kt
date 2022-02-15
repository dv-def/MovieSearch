package ru.dvn.moviesearch.model.movie

data class Movie(
    val name: String,
    val genre: String,
    val filmLength: Int,
    val rating: Double,
    val budget: Int,
    val revenue: Int,
    val releaseDate: String,
    val year: Int,
    var isFavorite: Boolean = false
)