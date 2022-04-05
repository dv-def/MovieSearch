package ru.dvn.moviesearch.utils

import ru.dvn.moviesearch.model.movie.detail.remote.MovieDetailDto
import ru.dvn.moviesearch.model.movie.detail.local.MovieEntity

fun convertDetailMovieDtoToMovieEntity(movieDetailDto: MovieDetailDto): MovieEntity {
    return with(movieDetailDto) {
        MovieEntity(
            kinopoiskId = kinopoiskId,
            name = nameRu,
            description = description,
            year = year,
            posterUrlPreview = posterUrlPreview
        )
    }
}


