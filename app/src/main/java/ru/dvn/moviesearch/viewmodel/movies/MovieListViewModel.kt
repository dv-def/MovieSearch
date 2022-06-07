package ru.dvn.moviesearch.viewmodel.movies

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.dvn.moviesearch.model.movie.remote.list.MovieListDataState
import ru.dvn.moviesearch.model.movie.remote.list.MovieListDto
import ru.dvn.moviesearch.model.movie.remote.list.repository.MovieListRepository
import ru.dvn.moviesearch.model.movie.remote.list.repository.MovieListRepositoryListImpl
import ru.dvn.moviesearch.model.movie.remote.list.TopParam
import java.lang.Exception

class MovieListViewModel : ViewModel() {
    private val liveData: MutableLiveData<MovieListDataState> = MutableLiveData()
    private val repository: MovieListRepository = MovieListRepositoryListImpl()

    private val callback = object: Callback<MovieListDto> {
    override fun onResponse(call: Call<MovieListDto>, response: Response<MovieListDto>) {
        val serverResponse = response.body()
        if (response.isSuccessful && serverResponse != null) {
            liveData.postValue(MovieListDataState.Success(serverResponse))
        } else {
            setErrorState("Не удалось получить данные с сервера")
        }
    }

    override fun onFailure(call: Call<MovieListDto>, t: Throwable) {
        setErrorState("При запросе данных произошла ошибка")
    }
}

    fun getLiveData(topParam: TopParam): MutableLiveData<MovieListDataState> {
        getMovieList(topParam)
        return liveData
    }

    fun getMovieList(topParam: TopParam) {
        repository.getMovieList(topParam, callback)
    }

    private fun setErrorState(message: String) {
        liveData.postValue(MovieListDataState.Error(Exception(message)))
    }
}