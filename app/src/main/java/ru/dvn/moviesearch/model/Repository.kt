package ru.dvn.moviesearch.model

import ru.dvn.moviesearch.model.movie.MovieList

interface Repository {
    fun getNowPlayingMovies(): MovieList
    fun getTopAwaitMovies(): MovieList
}