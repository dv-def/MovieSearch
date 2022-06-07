package ru.dvn.moviesearch.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.dvn.moviesearch.model.history.local.HISTORY_TABLE_NAME
import ru.dvn.moviesearch.model.history.local.HistoryEntity

@Dao
interface HistoryDao {
    @Query("SELECT * FROM $HISTORY_TABLE_NAME ORDER BY id DESC")
    fun getAll(): List<HistoryEntity>?

    @Insert
    fun save(historyEntity: HistoryEntity): Long
}