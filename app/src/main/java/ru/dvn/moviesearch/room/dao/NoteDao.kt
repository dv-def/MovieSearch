package ru.dvn.moviesearch.room.dao

import androidx.room.*
import ru.dvn.moviesearch.model.note.local.NoteEntity
import ru.dvn.moviesearch.model.note.local.TABLE_NAME_NOTES

@Dao
interface NoteDao {
    @Query("SELECT * FROM $TABLE_NAME_NOTES " +
            "WHERE kinopoisk_film_id = :kinopoiskFilmId " +
            "ORDER BY id DESC")
    fun getAllByKinopoiskId(kinopoiskFilmId: Long): List<NoteEntity>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(noteEntity: NoteEntity): Long

    @Delete
    fun delete(noteEntity: NoteEntity): Int
}