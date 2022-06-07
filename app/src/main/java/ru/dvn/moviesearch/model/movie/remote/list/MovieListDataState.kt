package ru.dvn.moviesearch.model.movie.remote.list

sealed class MovieListDataState {
    data class Success(val movies: MovieListDto) : MovieListDataState()
    data class Error(val error: Throwable) : MovieListDataState()
    object Loading : MovieListDataState()
}