package ru.dvn.moviesearch.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.dvn.moviesearch.model.AppState
import ru.dvn.moviesearch.model.MockRepository
import ru.dvn.moviesearch.model.Repository
import java.lang.Exception
import java.lang.Thread.sleep

class MovieViewModel : ViewModel() {
    private val nowPlayingLiveData: MutableLiveData<AppState> = MutableLiveData()
    private val upcomingLiveData: MutableLiveData<AppState> = MutableLiveData()

    private val repository: Repository = MockRepository()

    fun getNowPlayingLiveData() = nowPlayingLiveData

    fun getUpcomingLiveData() = upcomingLiveData

    fun getMoviesFromServer() {
        getDataFromServer()
    }

    fun getMoviesFromLocalStorage() {
        getDataFromLocalStorage()
    }

    // Симуляция успешного запроса к базе
    private fun getDataFromLocalStorage() {
        nowPlayingLiveData.value = AppState.Loading
        Thread {
            sleep(1000)
            nowPlayingLiveData.postValue(AppState.Success(repository.getMoviesFromLocalStorage()))
        }.start()
    }

    // Симуляция запроса к серверу - пока ошибка
    private fun getDataFromServer() {
        getDataWithError()
    }

    // Симуляция ошибки
    private fun getDataWithError() {
        nowPlayingLiveData.value = AppState.Loading
        Thread {
            sleep(2000)
            nowPlayingLiveData.postValue(AppState.Error(Exception("Test Error")))
        }.start()
    }
}