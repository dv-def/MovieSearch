package ru.dvn.moviesearch.model.movie.list

sealed class MovieListState {
    data class Success(val movies: MovieList) : MovieListState()
    data class Error(val error: Throwable) : MovieListState()
    object Loading : MovieListState()
}