package ru.dvn.moviesearch.model

import ru.dvn.moviesearch.model.history.HistoryEntity
import ru.dvn.moviesearch.model.movie.detail.remote.MovieDetailDto
import ru.dvn.moviesearch.model.movie.list.remote.MovieListDto
import ru.dvn.moviesearch.model.note.local.NoteEntity
import ru.dvn.moviesearch.model.staff.details.StaffDetailsDto
import ru.dvn.moviesearch.model.staff.list.StaffDto

sealed class AppState {
    data class SuccessMovieDetails(val movie: MovieDetailDto) : AppState()
    data class SuccessStaff(val staff: List<StaffDto>): AppState()
    data class SuccessStaffDetails(val staffDetails: StaffDetailsDto): AppState()

    // работа с локальной бд
    data class SuccessNotes(val notes: List<NoteEntity>) : AppState()
    data class SuccessDML(val result: Long, val message: String): AppState() // успешно создали, изменили, удалили
    data class SuccessHistory(val history: List<HistoryEntity>): AppState()

    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}
