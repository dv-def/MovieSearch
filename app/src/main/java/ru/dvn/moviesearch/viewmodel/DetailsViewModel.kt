package ru.dvn.moviesearch.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.dvn.moviesearch.model.movie.detail.DetailsRepository
import ru.dvn.moviesearch.model.movie.detail.DetailsState
import ru.dvn.moviesearch.model.movie.detail.DetailsRepositoryImpl
import ru.dvn.moviesearch.model.movie.detail.MovieDetailDto

class DetailsViewModel : ViewModel() {
    private val liveData = MutableLiveData<DetailsState>()
    private val repository: DetailsRepository = DetailsRepositoryImpl()

    private val callback = object: Callback<MovieDetailDto> {
        override fun onResponse(call: Call<MovieDetailDto>, response: Response<MovieDetailDto>) {
            val serverResponse = response.body()
            if (response.isSuccessful && serverResponse != null) {
                liveData.postValue(DetailsState.Success(serverResponse))
                liveData.postValue(DetailsState.Error(Exception("Server Error")))
            } else {
                liveData.postValue(DetailsState.Error(Exception("Server Error")))
            }
        }

        override fun onFailure(call: Call<MovieDetailDto>, t: Throwable) {
            liveData.postValue(DetailsState.Error(Exception("Bad Request")))
        }
    }

    fun getLiveData() = liveData

    fun getDetails(id: Int) {
        liveData.postValue(DetailsState.Loading)
        repository.getDetails(id, callback)
    }
}