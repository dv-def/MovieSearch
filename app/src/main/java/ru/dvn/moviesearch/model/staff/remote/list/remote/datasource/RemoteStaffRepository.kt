package ru.dvn.moviesearch.model.staff.remote.list.remote.datasource

import retrofit2.Callback
import ru.dvn.moviesearch.model.staff.remote.details.StaffDetailsDto
import ru.dvn.moviesearch.model.staff.remote.list.StaffDto
import ru.dvn.moviesearch.model.staff.remote.list.remote.StaffDataSource
import ru.dvn.moviesearch.model.staff.remote.list.remote.StaffRepository

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