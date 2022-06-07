package ru.dvn.moviesearch.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.dvn.moviesearch.App.Companion.getDatabase
import ru.dvn.moviesearch.model.AppState
import ru.dvn.moviesearch.model.history.HistoryEntity
import ru.dvn.moviesearch.model.history.HistoryRepository
import ru.dvn.moviesearch.model.history.HistoryRepositoryImpl
import java.lang.Exception

class HistoryViewModel(
    private val liveData: MutableLiveData<AppState> = MutableLiveData(),
    private val repository: HistoryRepository = HistoryRepositoryImpl(getDatabase().getHistoryDao())
) : ViewModel() {

    fun getLiveData() = liveData

    fun getAll() {
        liveData.postValue(AppState.Loading)
        repository.getAll()?.let {
            if (it.isNotEmpty()) {
                liveData.postValue(AppState.SuccessHistory(it))
            }
        }
    }

    fun save(historyEntity: HistoryEntity) {
        repository.save(historyEntity)
    }
}