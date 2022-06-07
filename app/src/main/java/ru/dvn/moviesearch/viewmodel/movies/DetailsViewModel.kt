package ru.dvn.moviesearch.viewmodel.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.dvn.moviesearch.model.movie.remote.detail.DetailsLoadState
import ru.dvn.moviesearch.model.movie.remote.detail.MovieDetailDto
import ru.dvn.moviesearch.model.movie.remote.detail.remote.repository.DetailsRemoteRepository
import ru.dvn.moviesearch.model.movie.remote.detail.remote.repository.DetailsRemoteRepositoryImpl

class DetailsViewModel(
    private val remoteRepository: DetailsRemoteRepository = DetailsRemoteRepositoryImpl()
) : ViewModel() {
    private val _liveData = MutableLiveData<DetailsLoadState>()
    val liveData: LiveData<DetailsLoadState> = _liveData


    private val callback = object: Callback<MovieDetailDto> {
        override fun onResponse(call: Call<MovieDetailDto>, response: Response<MovieDetailDto>) {
            val serverResponse = response.body()
            if (response.isSuccessful && serverResponse != null) {
                _liveData.postValue(DetailsLoadState.Success(serverResponse))
            } else {
                _liveData.postValue(DetailsLoadState.Error("Не удалось загрузить информацию о фильме"))
            }
        }

        override fun onFailure(call: Call<MovieDetailDto>, t: Throwable) {
            _liveData.postValue(DetailsLoadState.Error("При запросе данных произошла ошибка"))
        }
    }

    fun getDetails(id: Long) {
        _liveData.postValue(DetailsLoadState.Loading)
        remoteRepository.getDetails(id, callback)
    }
}