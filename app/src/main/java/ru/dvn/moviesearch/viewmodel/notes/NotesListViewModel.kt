package ru.dvn.moviesearch.viewmodel.notes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.dvn.moviesearch.App.Companion.getDatabase
import ru.dvn.moviesearch.model.note.local.NoteEntity
import ru.dvn.moviesearch.model.note.local.NotesListState
import ru.dvn.moviesearch.model.note.local.repository.NotesRepositoryImpl
import ru.dvn.moviesearch.model.note.local.repository.NotesRepository

class NotesListViewModel(
    private val repository: NotesRepository = NotesRepositoryImpl(getDatabase().getNotesDao()),
) : ViewModel() {

    private val _liveData: MutableLiveData<NotesListState> = MutableLiveData()
    val liveData: LiveData<NotesListState> = _liveData

    fun getAllByKinopoiskId(kinopoiskId: Long) {
        _liveData.postValue(NotesListState.Loading)
        Thread {
            try {
                repository.getAllByKinopoisId(kinopoiskId)?.let {
                    _liveData.postValue(NotesListState.SuccessGetAll(it))
                }
            } catch (e: Exception) {
                setErrorState("Не удалось загрузить заметки")
            }
        }.start()
    }

    fun delete(noteEntity: NoteEntity, position: Int) {
        _liveData.postValue(NotesListState.Loading)
        Thread {
            try {
                val result = repository.delete(noteEntity)
                if (result > 0) {
                    _liveData.postValue(NotesListState.SuccessDelete(position, "Заметка успешно удалена"))
                } else {
                    setErrorState("Не удалось удалить заметку")
                }
            } catch (e: Exception) {
                setErrorState("Не удалось удалить заметку")
            }
        }.start()
    }

    private fun setErrorState(message: String) {
        _liveData.postValue(NotesListState.Error(message))
    }
}