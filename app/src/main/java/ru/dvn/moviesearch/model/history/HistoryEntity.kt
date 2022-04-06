package ru.dvn.moviesearch.model.history

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import ru.dvn.moviesearch.model.movie.detail.local.MovieEntity

const val HISTORY_TABLE_NAME = "history"

@Entity(
    tableName = HISTORY_TABLE_NAME
)
data class HistoryEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long,

    @ColumnInfo(name = "kinopoisk_film_id")
    val kinopoiskFilmId: Long?,

    @ColumnInfo(name = "movie_name")
    val movieName: String?,

    @ColumnInfo(name = "movie_poster")
    val moviePoster: String?,

    val date: String?
)