package ru.dvn.moviesearch.model.note.local

sealed class NoteEditState {
    data class SuccessSave(val message: String) : NoteEditState()
    data class Error(val message: String) : NoteEditState()
    object Loading : NoteEditState()
}