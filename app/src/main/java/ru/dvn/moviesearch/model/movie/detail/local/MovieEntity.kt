package ru.dvn.moviesearch.model.movie.detail.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

const val TABLE_NAME_MOVIES = "movies"

@Entity(tableName = TABLE_NAME_MOVIES)
data class MovieEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "kinopoisk_id")
    val kinopoiskId: Long? = 0,

    val name: String? = "",

    val description: String? = "",

    val year: Int? = 0,

    val posterUrlPreview: String?,

    @ColumnInfo(name = "is_favorite")
    var isFavorite: Boolean = false
)