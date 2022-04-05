package ru.dvn.moviesearch.room

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.dvn.moviesearch.model.movie.detail.local.MovieDao
import ru.dvn.moviesearch.model.movie.detail.local.MovieEntity
import ru.dvn.moviesearch.model.note.local.NoteDao
import ru.dvn.moviesearch.model.note.local.NoteEntity

@Database(
    entities = [
        MovieEntity::class,
        NoteEntity::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class DataBase : RoomDatabase() {
    abstract fun getMovieDao(): MovieDao
    abstract fun getNotesDao(): NoteDao
}