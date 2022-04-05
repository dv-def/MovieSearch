package ru.dvn.moviesearch.model.note.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import ru.dvn.moviesearch.model.movie.detail.local.MovieEntity

const val TABLE_NAME_NOTES = "notes"

@Entity(
    tableName = TABLE_NAME_NOTES,
    foreignKeys = [
        ForeignKey(
            entity = MovieEntity::class,
            parentColumns = arrayOf("kinopoisk_id"),
            childColumns = arrayOf("kinopoisk_film_id"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,

    @ColumnInfo(name = "kinopoisk_film_id")
    val kinopoiskFilmId: Long,

    val title: String? = "",
    val text: String? = "",
    val date: String? = ""
)