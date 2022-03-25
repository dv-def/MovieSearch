package ru.dvn.moviesearch.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.dvn.moviesearch.model.movie.list.MovieListState
import ru.dvn.moviesearch.model.movie.list.MovieList
import ru.dvn.moviesearch.model.movie.list.MovieListRepository
import ru.dvn.moviesearch.model.movie.list.MovieListRepositoryListImpl
import ru.dvn.moviesearch.model.movie.list.MoviesLoadMode
import java.lang.Exception

class MovieListViewModel : ViewModel() {
    private val topBestLiveData: MutableLiveData<MovieListState> = MutableLiveData()
    private val topAwaitLiveData: MutableLiveData<MovieListState> = MutableLiveData()
    private val repository: MovieListRepository = MovieListRepositoryListImpl()

    private val callBackTopBest = object: Callback<MovieList> {
        override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
            val serverResponse = response.body()
            if (response.isSuccessful && serverResponse != null) {
                topBestLiveData.postValue(MovieListState.Success(serverResponse))
            } else {
                topBestLiveData.postValue(MovieListState.Error(Exception("Server Error")))
            }
        }

        override fun onFailure(call: Call<MovieList>, t: Throwable) {
            topBestLiveData.postValue(MovieListState.Error(Exception("Bad request")))
        }
    }

    private val callBackTopAwait = object: Callback<MovieList> {
        override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
            val serverResponse = response.body()
            if (response.isSuccessful && serverResponse != null) {
                topAwaitLiveData.postValue(MovieListState.Success(serverResponse))
            } else {
                topAwaitLiveData.postValue(MovieListState.Error(Exception("Server Error")))
            }
        }

        override fun onFailure(call: Call<MovieList>, t: Throwable) {
            topAwaitLiveData.postValue(MovieListState.Error(Exception("Bad request")))
        }
    }

    fun getTopBestLiveData() = topBestLiveData

    fun getTopAwaitLiveData() = topAwaitLiveData


    fun getMovieList(mode: MoviesLoadMode) {
        when (mode) {
            MoviesLoadMode.TOP_AWAIT_FILMS -> {
                topAwaitLiveData.postValue(MovieListState.Loading)
                repository.getMovieList(mode, callBackTopAwait)
            }
            else -> {
                topBestLiveData.postValue(MovieListState.Loading)
                repository.getMovieList(mode, callBackTopBest)
            }
        }
    }
}