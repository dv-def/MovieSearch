package ru.dvn.moviesearch.model.movie.detail.local

import androidx.room.*

@Dao
interface MovieDao {
    @Query("SELECT * FROM $TABLE_NAME_MOVIES WHERE kinopoisk_id = :kinopoiskId")
    fun findByKinopoiskId(kinopoiskId: Long): MovieEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(movieEntities: MovieEntity)

    @Delete
    fun delete(movieEntity: MovieEntity)
}