package ru.dvn.moviesearch.model.movie.nowplaying

sealed class NowPlayingAppState {
    data class Success(val nowPlayingMovieList: List<NowPlayingMovie>) : NowPlayingAppState()
    data class Error(val error: Throwable) : NowPlayingAppState()
    object Loading : NowPlayingAppState()
}