package ru.dvn.moviesearch.model.history.local

sealed class HistoryState {
    data class SuccessGetAll(val history: List<HistoryEntity>) : HistoryState()
    data class Error(val message: String) : HistoryState()
    object Loading : HistoryState()
}
