package ru.dvn.moviesearch.model.staff.list.remote

import retrofit2.Callback
import ru.dvn.moviesearch.BuildConfig
import ru.dvn.moviesearch.model.Retrofit
import ru.dvn.moviesearch.model.staff.details.StaffDetailsDto
import ru.dvn.moviesearch.model.staff.list.StaffDto

class RemoteStaffDataSource : StaffDataSource{
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