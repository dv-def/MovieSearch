package ru.dvn.moviesearch.model.movie

sealed class AppState {
    data class Success(val movies: List<Movie>) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}