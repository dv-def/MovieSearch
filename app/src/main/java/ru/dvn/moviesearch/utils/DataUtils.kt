package ru.dvn.moviesearch.utils

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date

fun getCurrentDate(): String =
    SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date().time)



