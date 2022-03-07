package ru.dvn.moviesearch.model.movie

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieList(
    val film: List<FilmDTO?>
): Parcelable

@Parcelize
data class FilmDTO (
    val kinopoiskId: Int?,
    val nameRu: String?,
    val posterUrl: String?,
    val posterUrlPreview: String?,
    val ratingKinopoisk: Int?,
    val ratingImdb: Int?,
    val year: Int?,
    val filmLength: Int?,
    val description: String?,
    val genres: List<GenreDTO>?
): Parcelable

@Parcelize
data class GenreDTO (
    val genre: String?
): Parcelable