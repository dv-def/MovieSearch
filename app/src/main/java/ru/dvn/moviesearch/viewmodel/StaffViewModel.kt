package ru.dvn.moviesearch.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.dvn.moviesearch.model.AppState
import ru.dvn.moviesearch.model.staff.details.StaffDetailsDto
import ru.dvn.moviesearch.model.staff.list.StaffDto
import ru.dvn.moviesearch.model.staff.list.remote.RemoteStaffRepository
import ru.dvn.moviesearch.model.staff.list.remote.StaffRepository

class StaffViewModel(
    private val liveData: MutableLiveData<AppState> = MutableLiveData(),
    private val repository: StaffRepository = RemoteStaffRepository()
) : ViewModel() {

    private val callbackStaffList = object: Callback<List<StaffDto>> {
        override fun onResponse(call: Call<List<StaffDto>>, response: Response<List<StaffDto>>) {
            val serverResponse = response.body()
            if (response.isSuccessful && serverResponse != null) {
                liveData.postValue(AppState.SuccessStaff(serverResponse))
            } else {
                setErrorState("Не удалось загрузить список актеров")
            }
        }

        override fun onFailure(call: Call<List<StaffDto>>, t: Throwable) {
            setErrorState("Не удалось загрузить список актеров")
        }
    }

    private val callbackPersonDetails = object: Callback<StaffDetailsDto> {
        override fun onResponse(call: Call<StaffDetailsDto>, response: Response<StaffDetailsDto>) {
            val serverResponse = response.body()
            if (response.isSuccessful && serverResponse != null) {
                liveData.postValue(AppState.SuccessStaffDetails(serverResponse))
            } else {
                setErrorState("Не удалось загрузить информацию об актере")
            }
        }

        override fun onFailure(call: Call<StaffDetailsDto>, t: Throwable) {
            setErrorState("Не удалось загрузить информацию об актере")
        }
    }

    fun getLiveData() = liveData

    fun getStaff(filmId: Long) {
        liveData.postValue(AppState.Loading)
        Thread {
            repository.getStaff(filmId, callbackStaffList)
        }.start()
    }

    fun getStaffDetails(personId: Long) {
        liveData.postValue(AppState.Loading)
        Thread {
            repository.getPersonDetails(personId, callbackPersonDetails)
        }.start()
    }

    private fun setErrorState(message: String) {
        liveData.postValue(AppState.Error(Exception(message)))
    }
}