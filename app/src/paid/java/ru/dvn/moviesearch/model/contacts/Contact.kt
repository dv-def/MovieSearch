package ru.dvn.moviesearch.model.contacts

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Contact(
    val name: String,
    val phoneNumber: String,
): Parcelable
