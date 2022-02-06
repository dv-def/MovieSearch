package ru.dvn.moviesearch.model

import ru.dvn.moviesearch.model.movie.nowplaying.NowPlayingMovie
import ru.dvn.moviesearch.model.movie.upcoming.UpcomingMovie

interface Repository {
    fun getNowPlayingMoviesFromServer(): List<NowPlayingMovie>
    fun getNowPlayingMoviesFromLocalStorage(): List<NowPlayingMovie>

    fun getUpcomingMoviesFromServer(): List<UpcomingMovie>
    fun getUpcomingMoviesFromLocalStorage(): List<UpcomingMovie>
}