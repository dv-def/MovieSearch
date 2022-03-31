package ru.dvn.moviesearch.model.movie

import ru.dvn.moviesearch.model.movie.detail.MovieDetailDto
import ru.dvn.moviesearch.model.movie.list.MovieListDto

sealed class AppState {
    data class SuccessList(val movies: MovieListDto) : AppState()
    data class SuccessDetails(val movie: MovieDetailDto) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}
