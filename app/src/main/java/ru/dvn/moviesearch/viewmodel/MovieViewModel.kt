package ru.dvn.moviesearch.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.dvn.moviesearch.model.Repository
import ru.dvn.moviesearch.model.movie.AppState
import java.util.*

class MovieViewModel : ViewModel() {
    private val nowPlayingLiveData: MutableLiveData<AppState> = MutableLiveData()
    private val upcomingLiveData: MutableLiveData<AppState> = MutableLiveData()
}