package ru.dvn.moviesearch.model.movie.detail

sealed class DetailsState {
    data class Success(val movie: MovieDetailDto) : DetailsState()
    data class Error(val error: Throwable) : DetailsState()
    object Loading : DetailsState()
}
