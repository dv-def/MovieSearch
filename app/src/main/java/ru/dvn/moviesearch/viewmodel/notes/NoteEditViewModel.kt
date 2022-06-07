package ru.dvn.moviesearch.viewmodel.notes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.dvn.moviesearch.App.Companion.getDatabase
import ru.dvn.moviesearch.model.note.local.NoteEditState
import ru.dvn.moviesearch.model.note.local.NoteEntity
import ru.dvn.moviesearch.model.note.local.repository.NotesRepository
import ru.dvn.moviesearch.model.note.local.repository.NotesRepositoryImpl
import java.lang.Exception

class NoteEditViewModel(
    private val repository: NotesRepository = NotesRepositoryImpl(getDatabase().getNotesDao())
) : ViewModel() {
    private val _livedata = MutableLiveData<NoteEditState>()
    val liveData: LiveData<NoteEditState> = _livedata

    fun save(note: NoteEntity) {
        _livedata.postValue(NoteEditState.Loading)
        Thread {
            try {
                val result = repository.save(note)
                if (result > 0L) {
                    _livedata.postValue(NoteEditState.SuccessSave("Заметка успешно сохранена"))
                }
            } catch (e: Exception) {
                _livedata.postValue(NoteEditState.Error("Не удалось сохранить заметку"))
            }
        }.start()
    }
}