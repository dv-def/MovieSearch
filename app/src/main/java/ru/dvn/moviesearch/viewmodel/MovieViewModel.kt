package ru.dvn.moviesearch.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.dvn.moviesearch.model.movie.nowplaying.NowPlayingAppState
import ru.dvn.moviesearch.model.MockRepository
import ru.dvn.moviesearch.model.Repository
import ru.dvn.moviesearch.model.movie.upcoming.UpcomingAppState
import java.lang.Exception
import java.lang.Thread.sleep

class MovieViewModel : ViewModel() {
    private val nowPlayingLiveData: MutableLiveData<NowPlayingAppState> = MutableLiveData()
    private val upcomingLiveData: MutableLiveData<UpcomingAppState> = MutableLiveData()

    private val repository: Repository = MockRepository()

    // now playing
    fun getNowPlayingLiveData() = nowPlayingLiveData

    fun getNowPlayingMoviesFromServer() {
        getNowPlayingFromServer()
    }

    fun getNowPlayingMoviesFromLocalStorage() {
        getNowPlayingFromLocalStorage()
    }


    // upcoming
    fun getUpcomingLiveData() = upcomingLiveData

    fun getUpcomingFromServer() {
        getUpcomingDataFromServer()
    }

    fun getUpcomingFromLocalStorage() {
        getUpcomingDataFromLocalStorage()
    }

    // Симуляция успешного запроса к базе
    private fun getNowPlayingFromLocalStorage() {
        nowPlayingLiveData.value = NowPlayingAppState.Loading
        Thread {
            sleep(1000)
            nowPlayingLiveData.postValue(NowPlayingAppState.Success(repository.getNowPlayingMoviesFromLocalStorage()))
        }.start()
    }

    private fun getUpcomingDataFromLocalStorage() {
        upcomingLiveData.value = UpcomingAppState.Loading
        Thread {
            sleep(1500)
            upcomingLiveData.postValue(UpcomingAppState.Success(repository.getUpcomingMoviesFromLocalStorage()))
        }.start()
    }

    // Симуляция запроса к серверу - пока ошибка
    private fun getNowPlayingFromServer() {
        getNowPlayingDataWithError()
    }

    private fun getUpcomingDataFromServer() {
        getUpcomingDataWithError()
    }

    // Симуляция ошибки
    private fun getNowPlayingDataWithError() {
        nowPlayingLiveData.value = NowPlayingAppState.Loading
        Thread {
            sleep(2000)
            nowPlayingLiveData.postValue(NowPlayingAppState.Error(Exception("Test Error")))
        }.start()
    }

    private fun getUpcomingDataWithError() {
        upcomingLiveData.value = UpcomingAppState.Loading
        Thread {
            sleep(2000)
            upcomingLiveData.postValue(UpcomingAppState.Error(Exception("Test Error")))
        }.start()
    }
}