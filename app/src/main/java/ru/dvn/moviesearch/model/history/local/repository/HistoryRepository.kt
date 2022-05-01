package ru.dvn.moviesearch.model.history.local.repository

import ru.dvn.moviesearch.model.history.local.HistoryEntity

interface HistoryRepository {
    fun getAll(): List<HistoryEntity>?
    fun save(historyEntity: HistoryEntity): Long
}