package ru.dvn.moviesearch.model.movie

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(
    val name: String,
    val genre: String,
    val filmLength: Int,
    val rating: Double,
    val budget: Int,
    val revenue: Int,
    val releaseDate: String,
    val year: Int,
    val description: String? = null,
    var isFavorite: Boolean = false
): Parcelable