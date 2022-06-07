package ru.dvn.moviesearch.model.note.local

sealed class NotesListState {
    data class SuccessGetAll(val notes: List<NoteEntity>) : NotesListState()
    data class SuccessDelete(val position: Int, val message: String) : NotesListState()

    data class Error(val message: String) : NotesListState()
    object Loading : NotesListState()
}
