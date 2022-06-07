package ru.dvn.moviesearch.model.movie.remote.list

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieListDto (
    val films: List<FilmDTO>?
): Parcelable

@Parcelize
data class FilmDTO (
    val filmId: Long?,
    val nameRu: String?,
    val year: Int?,
    val filmLength:String?,
    val rating: String?,
    val posterUrlPreview: String?
): Parcelable