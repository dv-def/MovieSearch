package ru.dvn.moviesearch.model.staff.remote

import ru.dvn.moviesearch.model.staff.remote.list.StaffDto

sealed class StaffListState {
    data class Success(val staff: List<StaffDto>) : StaffListState()
    data class Error(val message: String) : StaffListState()
    object Loading : StaffListState()
}
