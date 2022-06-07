package ru.dvn.moviesearch.model.movie.detail.remote

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieDetailDto(
    val kinopoiskId: Long?,
    val nameRu: String?,
    val posterUrl: String?,
    val ratingKinopoisk: Double?,
    val ratingAwait: Double?,
    val slogan: String?,
    val description: String?,
    val genres: List<GenreDto>?,
    val year: Int?,
    val filmLength: Int?,
    val posterUrlPreview: String?
): Parcelable

@Parcelize
data class GenreDto (
    val genre: String?
): Parcelable