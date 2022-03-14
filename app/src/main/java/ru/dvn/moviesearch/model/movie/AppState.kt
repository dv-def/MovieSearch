package ru.dvn.moviesearch.model.movie

import ru.dvn.moviesearch.model.movie.list.MovieList

sealed class AppState {
    data class Success(val movies: MovieList) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}