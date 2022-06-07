package ru.dvn.moviesearch.model.note.local.repository

import ru.dvn.moviesearch.room.dao.NoteDao
import ru.dvn.moviesearch.model.note.local.NoteEntity

class NotesRepositoryImpl(
    private val noteDao: NoteDao
) : NotesRepository {
    override fun getAllByKinopoisId(kinopoiskId: Long): List<NoteEntity>? {
        return noteDao.getAllByKinopoiskId(kinopoiskId)
    }

    override fun save(noteEntity: NoteEntity): Long {
        return noteDao.save(noteEntity)
    }

    override fun delete(noteEntity: NoteEntity): Int {
        return noteDao.delete(noteEntity)
    }
}