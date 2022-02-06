package ru.dvn.moviesearch.model

import ru.dvn.moviesearch.model.movie.Movie

interface Repository {
    fun getMoviesFromServer(): List<Movie>
    fun getMoviesFromLocalStorage(): List<Movie>
}