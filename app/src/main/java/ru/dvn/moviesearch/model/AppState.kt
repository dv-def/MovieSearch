package ru.dvn.moviesearch.model

import ru.dvn.moviesearch.model.movie.detail.remote.MovieDetailDto
import ru.dvn.moviesearch.model.movie.list.remote.MovieListDto
import ru.dvn.moviesearch.model.note.local.NoteEntity

sealed class AppState {
    data class SuccessList(val movies: MovieListDto) : AppState()
    data class SuccessDetails(val movie: MovieDetailDto) : AppState()
    data class SuccessNotes(val notes: List<NoteEntity>) : AppState()
    data class SuccessDML(val result: Long, val message: String): AppState() // успешно создали, изменили, удалили
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}
