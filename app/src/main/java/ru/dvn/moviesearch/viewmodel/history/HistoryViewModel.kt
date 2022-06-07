package ru.dvn.moviesearch.viewmodel.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.dvn.moviesearch.App.Companion.getDatabase
import ru.dvn.moviesearch.model.history.local.HistoryEntity
import ru.dvn.moviesearch.model.history.local.HistoryState
import ru.dvn.moviesearch.model.history.local.repository.HistoryRepository
import ru.dvn.moviesearch.model.history.local.repository.HistoryRepositoryImpl
import java.lang.Exception

class HistoryViewModel(
    private val repository: HistoryRepository = HistoryRepositoryImpl(getDatabase().getHistoryDao())
) : ViewModel() {

    private val _liveData: MutableLiveData<HistoryState> = MutableLiveData()
    val liveData: LiveData<HistoryState> = _liveData

    fun getAll() {
        _liveData.postValue(HistoryState.Loading)
        Thread {
            try {
                repository.getAll()?.let {
                    _liveData.postValue(HistoryState.SuccessGetAll(it))
                }
            } catch (e: Exception) {
                _liveData.postValue(HistoryState.Error("Не удалось загрузить историю просмотров"))
            }
        }.start()

    }

    fun save(historyEntity: HistoryEntity) {
        Thread {
            try {
                repository.save(historyEntity)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }
}