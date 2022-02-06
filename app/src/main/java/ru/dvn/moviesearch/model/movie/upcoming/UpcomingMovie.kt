package ru.dvn.moviesearch.model.movie.upcoming

import java.util.*

data class UpcomingMovie(
    val name: String,
    val date: String,
    var isFavorite: Boolean = false
)