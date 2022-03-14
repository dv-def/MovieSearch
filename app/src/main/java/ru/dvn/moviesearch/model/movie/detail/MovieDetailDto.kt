package ru.dvn.moviesearch.model.movie.detail

data class MovieDetailDto(
    val kinopoiskId: Int?,
    val nameRu: String?,
    val posterUrl: String?,
    val ratingKinopoisk: Double?,
    val ratingAwait: Double?,
    val slogan: String?,
    val description: String?,
    val genres: List<GenreDto>?,
    val year: Int?,
    val filmLength: Int?
)

data class GenreDto (
    val genre: String?
)