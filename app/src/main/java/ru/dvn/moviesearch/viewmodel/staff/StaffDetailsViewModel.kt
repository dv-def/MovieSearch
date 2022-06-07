package ru.dvn.moviesearch.viewmodel.staff

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.dvn.moviesearch.model.staff.remote.StaffDetailsState
import ru.dvn.moviesearch.model.staff.remote.details.StaffDetailsDto
import ru.dvn.moviesearch.model.staff.remote.list.remote.StaffRepository
import ru.dvn.moviesearch.model.staff.remote.list.remote.datasource.RemoteStaffRepository

class StaffDetailsViewModel(
    private val repository: StaffRepository = RemoteStaffRepository()
) : ViewModel() {
    private val _liveData = MutableLiveData<StaffDetailsState>()
    val liveData: LiveData<StaffDetailsState> = _liveData

    private val callback = object : Callback<StaffDetailsDto> {
        override fun onResponse(call: Call<StaffDetailsDto>, response: Response<StaffDetailsDto>) {
            val serverResponse = response.body()
            if (response.isSuccessful && serverResponse != null) {
                _liveData.postValue(StaffDetailsState.Success(serverResponse))
            } else {
                setErrorState()
            }
        }

        override fun onFailure(call: Call<StaffDetailsDto>, t: Throwable) {
            setErrorState()
        }
    }

    fun getStaffDetails(personId: Long) {
        _liveData.postValue(StaffDetailsState.Loading)
        repository.getPersonDetails(personId, callback)
    }

    private fun setErrorState() {
        _liveData.postValue(StaffDetailsState.Error(ERROR_MESSAGE))
    }

    companion object {
        const val ERROR_MESSAGE = "Не удалось загрузить список актеров"
    }
}