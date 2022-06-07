package ru.dvn.moviesearch.model.history

class HistoryRepositoryImpl(
    private val historyDao: HistoryDao
) : HistoryRepository {
    override fun getAll(): List<HistoryEntity>? = historyDao.getAll()

    override fun save(historyEntity: HistoryEntity): Long = historyDao.save(historyEntity)
}