package ru.dvn.moviesearch.model

interface Repository {
    fun getMoviesFromServer(): List<Movie>
    fun getMoviesFromLocalStorage(): List<Movie>
}