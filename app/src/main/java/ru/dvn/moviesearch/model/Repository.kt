package ru.dvn.moviesearch.model

import ru.dvn.moviesearch.model.movie.list.MovieList

interface Repository {
    fun getNowPlayingMovies(): MovieList
    fun getTopAwaitMovies(): MovieList
}