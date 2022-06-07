package ru.dvn.moviesearch.model.note.local

import androidx.room.*

@Dao
interface NoteDao {
    @Query("SELECT * FROM $TABLE_NAME_NOTES " +
            "WHERE kinopoisk_film_id = :kinopoiskFilmId " +
            "ORDER BY date DESC")
    fun getAllByKinopoiskId(kinopoiskFilmId: Long): List<NoteEntity>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(noteEntity: NoteEntity): Long

    @Delete
    fun delete(noteEntity: NoteEntity): Int
}