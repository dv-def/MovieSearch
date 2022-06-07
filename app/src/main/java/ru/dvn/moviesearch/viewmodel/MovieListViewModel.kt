package ru.dvn.moviesearch.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.dvn.moviesearch.model.AppState
import ru.dvn.moviesearch.model.movie.list.remote.MovieListDto
import ru.dvn.moviesearch.model.movie.list.remote.MovieListRepository
import ru.dvn.moviesearch.model.movie.list.remote.MovieListRepositoryListImpl
import ru.dvn.moviesearch.model.movie.list.remote.MoviesLoadMode
import java.lang.Exception

class MovieListViewModel : ViewModel() {
    private val topBestLiveData: MutableLiveData<AppState> = MutableLiveData()
    private val topAwaitLiveData: MutableLiveData<AppState> = MutableLiveData()
    private val repository: MovieListRepository = MovieListRepositoryListImpl()

    private val callBackTopBest = object: Callback<MovieListDto> {
        override fun onResponse(call: Call<MovieListDto>, response: Response<MovieListDto>) {
            val serverResponse = response.body()
            if (response.isSuccessful && serverResponse != null) {
                topBestLiveData.postValue(AppState.SuccessList(serverResponse))
            } else {
                topBestLiveData.postValue(AppState.Error(Exception("Server Error")))
            }
        }

        override fun onFailure(call: Call<MovieListDto>, t: Throwable) {
            topBestLiveData.postValue(AppState.Error(Exception("Bad request")))
        }
    }

    private val callBackTopAwait = object: Callback<MovieListDto> {
        override fun onResponse(call: Call<MovieListDto>, response: Response<MovieListDto>) {
            val serverResponse = response.body()
            if (response.isSuccessful && serverResponse != null) {
                topAwaitLiveData.postValue(AppState.SuccessList(serverResponse))
            } else {
                topAwaitLiveData.postValue(AppState.Error(Exception("Server Error")))
            }
        }

        override fun onFailure(call: Call<MovieListDto>, t: Throwable) {
            topAwaitLiveData.postValue(AppState.Error(Exception("Bad request")))
        }
    }

    fun getTopBestLiveData() = topBestLiveData

    fun getTopAwaitLiveData() = topAwaitLiveData


    fun getMovieList(mode: MoviesLoadMode) {
        when (mode) {
            MoviesLoadMode.TOP_AWAIT_FILMS -> {
                topAwaitLiveData.postValue(AppState.Loading)
                repository.getMovieList(mode, callBackTopAwait)
            }
            else -> {
                topBestLiveData.postValue(AppState.Loading)
                repository.getMovieList(mode, callBackTopBest)
            }
        }
    }
}