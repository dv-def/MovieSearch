package ru.dvn.moviesearch.model.staff.details

import ru.dvn.moviesearch.model.staff.Profession

data class StaffDetailsDto (
    val personId: Long? = 0L,
    val nameRu: String? = "",
    val nameEn: String? = "",
    val posterUrl: String? = "",
    val birthday: String? = "",
    val death: String? = "",
    val age: Int? = 0,
    val birthplace: String? = "",
    val deathplace: String? = "",
    val profession: String? = "",
    val facts: List<String>?,
    val films: List<PersonFilm>?
)

data class PersonFilm (
    val filmId: Long? = 0L,
    val nameRu: String? = "",
    val nameEn: String? = "",
    val rating: String? = "",
    val description: String? = "",
    val professionKey: Profession?
)