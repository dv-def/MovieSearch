package ru.dvn.moviesearch.model

import ru.dvn.moviesearch.model.movie.Movie

interface Repository {
    fun getMoviesNowPlayingFromServer(): List<Movie>
    fun getMoviesNowPlayingLocalStorage(): List<Movie>

    fun getMoviesUpcomingFromServer(): List<Movie>
    fun getMoviesUpcomingFromLocalStorage(): List<Movie>
}