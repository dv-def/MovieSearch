package ru.dvn.moviesearch.model.staff.remote.list

import ru.dvn.moviesearch.model.staff.Profession

data class StaffDto (
    val staffId: Long? = 0L,
    val nameRu: String? = "",
    val posterUrl: String? = "",
    val professionKey: Profession?,
)