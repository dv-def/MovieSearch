package ru.dvn.moviesearch.model.staff.list.remote

import retrofit2.Callback
import ru.dvn.moviesearch.model.staff.details.StaffDetailsDto
import ru.dvn.moviesearch.model.staff.list.StaffDto

class RemoteStaffRepository(
    private val dataSource: StaffDataSource = RemoteStaffDataSource()
) : StaffRepository {

    override fun getStaff(filmId: Long, callback: Callback<List<StaffDto>>) {
        dataSource.getStaff(filmId, callback)
    }

    override fun getPersonDetails(personId: Long, callback: Callback<StaffDetailsDto>) {
        dataSource.getPersonDetails(personId, callback)
    }
}