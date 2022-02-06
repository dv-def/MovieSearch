package ru.dvn.moviesearch.model

import ru.dvn.moviesearch.model.movie.nowplaying.NowPlayingMovie
import ru.dvn.moviesearch.model.movie.upcoming.UpcomingMovie
import java.text.DateFormat
import java.text.SimpleDateFormat

class MockRepository : Repository {
    override fun getNowPlayingMoviesFromServer(): List<NowPlayingMovie> {
        return ArrayList<NowPlayingMovie>()
    }

    override fun getNowPlayingMoviesFromLocalStorage(): List<NowPlayingMovie> {
        return getFilledNowPlayingArray()
    }

    override fun getUpcomingMoviesFromServer(): List<UpcomingMovie> {
        return ArrayList<UpcomingMovie>()
    }

    override fun getUpcomingMoviesFromLocalStorage(): List<UpcomingMovie> {
        return getFilledUpcomingArray()
    }

    private fun getFilledNowPlayingArray(): List<NowPlayingMovie> {
        return listOf(
            NowPlayingMovie("Эмма", "2020", 6.9, false),
            NowPlayingMovie("Вивариум", "2019", 5.7, true),
            NowPlayingMovie("Воспоминания об убийстве", "2020", 8.1, false),
            NowPlayingMovie("Почти знамениты", "2020", 7.2, false)
        )
    }

    private fun getFilledUpcomingArray(): List<UpcomingMovie> {
        return listOf(
            UpcomingMovie("Первая ведьма", "2020-05-28", false),
            UpcomingMovie("Приди ко мне", "2020-05-21", false),
            UpcomingMovie("Почти знамениты", "2020-05-28", false)
        )
    }
}