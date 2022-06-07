package ru.dvn.moviesearch.room

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.dvn.moviesearch.model.history.HistoryDao
import ru.dvn.moviesearch.model.history.HistoryEntity
import ru.dvn.moviesearch.model.note.local.NoteDao
import ru.dvn.moviesearch.model.note.local.NoteEntity

@Database(
    entities = [
        NoteEntity::class,
        HistoryEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class DataBase : RoomDatabase() {
    abstract fun getNotesDao(): NoteDao
    abstract fun getHistoryDao(): HistoryDao
}