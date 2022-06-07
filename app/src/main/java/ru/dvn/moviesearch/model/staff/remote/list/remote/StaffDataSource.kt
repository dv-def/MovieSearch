package ru.dvn.moviesearch.model.staff.remote.list.remote

import retrofit2.Callback
import ru.dvn.moviesearch.model.staff.remote.details.StaffDetailsDto
import ru.dvn.moviesearch.model.staff.remote.list.StaffDto

interface StaffDataSource {
    fun getStaff(filmId: Long, callback: Callback<List<StaffDto>>)
    fun getPersonDetails(personId: Long, callback: Callback<StaffDetailsDto>)
}