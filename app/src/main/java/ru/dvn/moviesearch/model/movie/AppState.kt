package ru.dvn.moviesearch.model.movie

import ru.dvn.moviesearch.model.movie.detail.MovieDetailDto
import ru.dvn.moviesearch.model.movie.list.MovieList

sealed class AppState {
    data class SuccessList(val movies: MovieList) : AppState()
    data class SuccessDetails(val movie: MovieDetailDto) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}
