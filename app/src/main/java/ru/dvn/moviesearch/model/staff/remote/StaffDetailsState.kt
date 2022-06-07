package ru.dvn.moviesearch.model.staff.remote

import ru.dvn.moviesearch.model.staff.remote.details.StaffDetailsDto

sealed class StaffDetailsState {
    data class Success(val staffDetails: StaffDetailsDto) : StaffDetailsState()
    data class Error(val message: String) : StaffDetailsState()
    object Loading : StaffDetailsState()
}
