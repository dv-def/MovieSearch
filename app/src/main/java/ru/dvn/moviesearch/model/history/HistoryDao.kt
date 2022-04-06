package ru.dvn.moviesearch.model.history

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface HistoryDao {
    @Query("SELECT * FROM $HISTORY_TABLE_NAME ORDER BY id DESC")
    fun getAll(): List<HistoryEntity>?

    @Insert
    fun save(historyEntity: HistoryEntity): Long
}