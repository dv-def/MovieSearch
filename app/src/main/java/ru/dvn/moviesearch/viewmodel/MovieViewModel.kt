package ru.dvn.moviesearch.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.dvn.moviesearch.model.AppState
import ru.dvn.moviesearch.model.MockRepository
import ru.dvn.moviesearch.model.Repository
import java.lang.Exception
import java.lang.Thread.sleep

class MovieViewModel : ViewModel() {
    private val liveDataForObserve: MutableLiveData<AppState> = MutableLiveData<AppState>()
    private val repository: Repository = MockRepository()

    fun getLiveData() = liveDataForObserve

    fun getMoviesFromServer() {
        getDataFromServer()
    }

    fun getMoviesFromLocalStorage() {
        getDataFromLocalStorage()
    }

    // Симуляция успешного запроса к базе
    private fun getDataFromLocalStorage() {
        liveDataForObserve.value = AppState.Loading
        Thread {
            sleep(1000)
            liveDataForObserve.postValue(AppState.Success(repository.getMoviesFromLocalStorage()))
        }.start()
    }

    // Симуляция запроса к серверу - пока ошибка
    private fun getDataFromServer() {
        getDataWithError()
    }

    // Симуляция ошибки
    private fun getDataWithError() {
        liveDataForObserve.value = AppState.Loading
        Thread {
            sleep(2000)
            liveDataForObserve.postValue(AppState.Error(Exception("Test Error")))
        }.start()
    }
}