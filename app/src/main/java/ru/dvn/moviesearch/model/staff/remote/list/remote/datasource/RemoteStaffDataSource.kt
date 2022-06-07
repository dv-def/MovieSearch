package ru.dvn.moviesearch.model.staff.remote.list.remote.datasource

import retrofit2.Callback
import ru.dvn.moviesearch.BuildConfig
import ru.dvn.moviesearch.model.Retrofit
import ru.dvn.moviesearch.model.staff.remote.details.StaffDetailsDto
import ru.dvn.moviesearch.model.staff.remote.list.StaffDto
import ru.dvn.moviesearch.model.staff.remote.list.remote.StaffDataSource

class RemoteStaffDataSource : StaffDataSource {
    override fun getStaff(filmId: Long, callback: Callback<List<StaffDto>>) {
        Retrofit.getStaffApi()
            .getStaff(BuildConfig.MOVIES_API_KEY, filmId)
            .enqueue(callback)
    }

    override fun getPersonDetails(personId: Long, callback: Callback<StaffDetailsDto>) {
        Retrofit.getStaffApi()
            .getStaffDetail(BuildConfig.MOVIES_API_KEY, personId)
            .enqueue(callback)
    }
}