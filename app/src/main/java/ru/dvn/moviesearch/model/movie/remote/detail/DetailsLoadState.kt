package ru.dvn.moviesearch.model.movie.remote.detail

sealed class DetailsLoadState {
    data class Success(val movie: MovieDetailDto) : DetailsLoadState()
    data class Error(val message: String) : DetailsLoadState()
    object Loading : DetailsLoadState()
}