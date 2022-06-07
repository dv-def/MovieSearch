package ru.dvn.moviesearch.model.history.local.repository

import ru.dvn.moviesearch.model.history.local.HistoryEntity
import ru.dvn.moviesearch.room.dao.HistoryDao

class HistoryRepositoryImpl(
    private val historyDao: HistoryDao
) : HistoryRepository {
    override fun getAll(): List<HistoryEntity>? = historyDao.getAll()

    override fun save(historyEntity: HistoryEntity): Long = historyDao.save(historyEntity)
}