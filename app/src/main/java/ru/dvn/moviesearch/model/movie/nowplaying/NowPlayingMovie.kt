package ru.dvn.moviesearch.model.movie.nowplaying

data class NowPlayingMovie(
    val name: String,
    val year: String,
    val rating: Double,
    var isFavorite: Boolean = false
)