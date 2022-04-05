package ru.dvn.moviesearch.model.note.local

interface NotesRepository {
    fun getAllByKinopoisId(kinopoiskId: Long): List<NoteEntity>?
    fun save(noteEntity: NoteEntity): Long
    fun delete(noteEntity: NoteEntity): Int
}