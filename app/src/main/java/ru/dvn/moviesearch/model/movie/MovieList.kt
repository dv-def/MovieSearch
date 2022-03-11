package ru.dvn.moviesearch.model.movie

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieList(
    val films: List<FilmDTO>?
): Parcelable

@Parcelize
data class FilmDTO (
    val filmId: Int?,
    val nameRu: String?,
    val year: Int?,
    val filmLength:String?,
    val rating: Double?,
    val posterUrlPreview: String?
): Parcelable