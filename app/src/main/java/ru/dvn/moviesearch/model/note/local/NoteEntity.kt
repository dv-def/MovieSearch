package ru.dvn.moviesearch.model.note.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

const val TABLE_NAME_NOTES = "notes"

@Entity(
    tableName = TABLE_NAME_NOTES
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