package ru.dvn.moviesearch.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.dvn.moviesearch.model.movie.AppState
import ru.dvn.moviesearch.model.movie.detail.DetailsRepository
import ru.dvn.moviesearch.model.movie.detail.DetailsRepositoryImpl
import ru.dvn.moviesearch.model.movie.detail.MovieDetailDto

class DetailsViewModel : ViewModel() {
    private val liveData = MutableLiveData<AppState>()
    private val repository: DetailsRepository = DetailsRepositoryImpl()

    private val callback = object: Callback<MovieDetailDto> {
        override fun onResponse(call: Call<MovieDetailDto>, response: Response<MovieDetailDto>) {
            val serverResponse = response.body()
            if (response.isSuccessful && serverResponse != null) {
                liveData.postValue(AppState.SuccessDetails(serverResponse))
            } else {
                liveData.postValue(AppState.Error(Exception("Server Error")))
            }
        }

        override fun onFailure(call: Call<MovieDetailDto>, t: Throwable) {
            liveData.postValue(AppState.Error(Exception("Bad Request")))
        }
    }

    fun getLiveData() = liveData

    fun getDetails(id: Int) {
        liveData.postValue(AppState.Loading)
        repository.getDetails(id, callback)
    }
}