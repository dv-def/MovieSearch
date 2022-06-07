package ru.dvn.moviesearch.model.movie.list.remote

enum class MoviesLoadMode(private val mode: String) {
    TOP_BEST_FILMS("TOP_250_BEST_FILMS"),
    TOP_AWAIT_FILMS("TOP_AWAIT_FILMS");

    fun getMode() = mode
}