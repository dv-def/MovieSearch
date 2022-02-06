package ru.dvn.moviesearch.model.movie.upcoming

sealed class UpcomingAppState {
    data class Success(val movieList: List<UpcomingMovie>) : UpcomingAppState()
    data class Error(val error: Throwable) : UpcomingAppState()
    object Loading : UpcomingAppState()
}