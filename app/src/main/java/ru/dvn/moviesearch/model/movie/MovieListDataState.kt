package ru.dvn.moviesearch.model.movie

import ru.dvn.moviesearch.model.movie.list.remote.MovieListDto


sealed class MovieListDataState {
    data class Success(val movies: MovieListDto) : MovieListDataState()
    data class Error(val error: Throwable) : MovieListDataState()
    object Loading : MovieListDataState()
}