package ru.dvn.moviesearch.model

sealed class AppState {
    data class Success(val movieList: List<Movie>) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}