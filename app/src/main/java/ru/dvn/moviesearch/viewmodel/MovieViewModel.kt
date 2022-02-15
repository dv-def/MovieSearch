package ru.dvn.moviesearch.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.dvn.moviesearch.model.MockRepository
import ru.dvn.moviesearch.model.Repository
import ru.dvn.moviesearch.model.movie.AppState
import java.lang.Exception
import java.lang.Thread.sleep
import java.util.*

class MovieViewModel : ViewModel() {
    private val nowPlayingLiveData: MutableLiveData<AppState> = MutableLiveData()
    private val upcomingLiveData: MutableLiveData<AppState> = MutableLiveData()

    private val repository: Repository = MockRepository()

    // now playing
    fun getNowPlayingLiveData() = nowPlayingLiveData

    fun getNowPlayingMoviesFromServer() = getNowPlayingFromServer()

    fun getNowPlayingMoviesFromLocalStorage() =  getNowPlayingFromLocalStorage()


    // upcoming
    fun getUpcomingLiveData() = upcomingLiveData

    fun getUpcomingFromServer() = getUpcomingDataFromServer()

    fun getUpcomingFromLocalStorage() = getUpcomingDataFromLocalStorage()

    // Симуляция успешного запроса к базе
    private fun getNowPlayingFromLocalStorage() {
        nowPlayingLiveData.value = AppState.Loading
        Thread {
            sleep(1000)
            if (getRandomNumber() > 2) {
                nowPlayingLiveData.postValue(AppState.Success(repository.getMoviesNowPlayingLocalStorage()))
            } else {
                nowPlayingLiveData.postValue(AppState.Error(Exception("By Random :)")))
            }
        }.start()
    }

    private fun getUpcomingDataFromLocalStorage() {
        upcomingLiveData.value = AppState.Loading
        Thread {
            sleep(1500)
            if (getRandomNumber() > 2) {
                upcomingLiveData.postValue(AppState.Success(repository.getMoviesUpcomingFromLocalStorage()))
            } else {
                upcomingLiveData.postValue(AppState.Error(Exception("By Random :)")))
            }
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
        nowPlayingLiveData.value = AppState.Loading
        Thread {
            sleep(2000)
            nowPlayingLiveData.postValue(AppState.Error(Exception("Test Error")))
        }.start()
    }

    private fun getUpcomingDataWithError() {
        upcomingLiveData.value = AppState.Loading
        Thread {
            sleep(2000)
            upcomingLiveData.postValue(AppState.Error(Exception("Test Error")))
        }.start()
    }

    private fun getRandomNumber(): Int {
        return Random().nextInt(9) + 1
    }
}