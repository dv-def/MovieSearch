package ru.dvn.moviesearch.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.dvn.moviesearch.App.Companion.getDatabase
import ru.dvn.moviesearch.model.AppState
import ru.dvn.moviesearch.model.note.local.NoteEntity
import ru.dvn.moviesearch.model.note.local.NoteRepositoryImpl
import ru.dvn.moviesearch.model.note.local.NotesRepository
import java.lang.Exception

class NotesViewModel(
    private val repository: NotesRepository = NoteRepositoryImpl(getDatabase().getNotesDao()),
    private val liveData: MutableLiveData<AppState> = MutableLiveData()
) : ViewModel() {
    fun getLiveData(): MutableLiveData<AppState> = liveData

    fun getAllByKinopoiskId(kinopoiskId: Long) {
        liveData.postValue(AppState.Loading)
        repository.getAllByKinopoisId(kinopoiskId)?.let {
            if (it.isNotEmpty()) {
                liveData.postValue(AppState.SuccessNotes(it))
            }
        }
    }

    fun save(noteEntity: NoteEntity) {
        val result = repository.save(noteEntity)
        setDMLStatus(result, "Заметка успешно сохранена", "Не удалось сохранить заметку")
    }

    fun delete(noteEntity: NoteEntity) {
        val result = repository.delete(noteEntity)
        setDMLStatus(result.toLong(), "Заметка успешно удалена", "Не удалось удалить заметку")
    }

    // Создали, изменили, удалили
    private fun setDMLStatus(result: Long, message: String, error: String) {
        liveData.postValue(
            if (result > -1L) {
                AppState.SuccessDML(result, message)
            } else {
                AppState.Error(Exception(error))
            }
        )
    }
}