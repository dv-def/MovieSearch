package ru.dvn.moviesearch.model.staff.list.remote

import retrofit2.Callback
import ru.dvn.moviesearch.model.staff.details.StaffDetailsDto
import ru.dvn.moviesearch.model.staff.list.StaffDto

interface StaffRepository {
    fun getStaff(filmId: Long, callback: Callback<List<StaffDto>>)
    fun getPersonDetails(personId: Long, callback: Callback<StaffDetailsDto>)
}