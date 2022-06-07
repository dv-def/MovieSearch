package ru.dvn.moviesearch.viewmodel.staff

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.dvn.moviesearch.model.staff.remote.StaffListState
import ru.dvn.moviesearch.model.staff.remote.list.StaffDto
import ru.dvn.moviesearch.model.staff.remote.list.remote.datasource.RemoteStaffRepository
import ru.dvn.moviesearch.model.staff.remote.list.remote.StaffRepository

class StaffListViewModel(
    private val repository: StaffRepository = RemoteStaffRepository()
) : ViewModel() {

    private val _liveData: MutableLiveData<StaffListState> = MutableLiveData()
    val liveData: LiveData<StaffListState> = _liveData

    private val callback = object: Callback<List<StaffDto>> {
        override fun onResponse(call: Call<List<StaffDto>>, response: Response<List<StaffDto>>) {
            val serverResponse = response.body()
            if (response.isSuccessful && serverResponse != null) {
                _liveData.postValue(StaffListState.Success(serverResponse))
            } else {
                setErrorState()
            }
        }

        override fun onFailure(call: Call<List<StaffDto>>, t: Throwable) {
            setErrorState()
        }
    }

    fun getStaff(filmId: Long) {
        _liveData.postValue(StaffListState.Loading)
        repository.getStaff(filmId, callback)
    }

    private fun setErrorState() {
        _liveData.postValue(StaffListState.Error(ERROR_MESSAGE))
    }

    companion object {
        const val ERROR_MESSAGE = "Не удалось загрузить список актеров"
    }
}