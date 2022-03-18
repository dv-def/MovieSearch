package ru.dvn.moviesearch.model.movie.detail

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
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
): Parcelable

@Parcelize
data class GenreDto (
    val genre: String?
): Parcelable