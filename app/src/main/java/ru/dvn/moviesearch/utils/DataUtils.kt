package ru.dvn.moviesearch.utils

import ru.dvn.moviesearch.model.movie.detail.remote.MovieDetailDto
import ru.dvn.moviesearch.model.movie.detail.local.MovieEntity
import java.text.SimpleDateFormat
import java.util.*

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

fun getCurrentDate(): String =
    SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date().time)



