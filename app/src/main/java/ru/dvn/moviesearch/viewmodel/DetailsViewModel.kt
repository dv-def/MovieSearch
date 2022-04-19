package ru.dvn.moviesearch.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.dvn.moviesearch.model.AppState
import ru.dvn.moviesearch.model.movie.detail.remote.DetailsRemoteRepository
import ru.dvn.moviesearch.model.movie.detail.remote.DetailsRemoteRepositoryImpl
import ru.dvn.moviesearch.model.movie.detail.remote.MovieDetailDto

class DetailsViewModel : ViewModel() {
    private val liveData = MutableLiveData<AppState>()
    private val remoteRepository: DetailsRemoteRepository = DetailsRemoteRepositoryImpl()

    private val callback = object: Callback<MovieDetailDto> {
        override fun onResponse(call: Call<MovieDetailDto>, response: Response<MovieDetailDto>) {
            val serverResponse = response.body()
            if (response.isSuccessful && serverResponse != null) {
                liveData.postValue(AppState.SuccessMovieDetails(serverResponse))
            } else {
                liveData.postValue(AppState.Error(Exception("Server Error")))
            }
        }

        override fun onFailure(call: Call<MovieDetailDto>, t: Throwable) {
            liveData.postValue(AppState.Error(Exception("Bad Request")))
        }
    }

    fun getLiveData() = liveData

    fun getDetails(id: Long) {
        liveData.postValue(AppState.Loading)
        remoteRepository.getDetails(id, callback)
    }
}