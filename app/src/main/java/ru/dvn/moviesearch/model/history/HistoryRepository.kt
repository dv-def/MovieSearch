package ru.dvn.moviesearch.model.history

interface HistoryRepository {
    fun getAll(): List<HistoryEntity>?
    fun save(historyEntity: HistoryEntity): Long
}