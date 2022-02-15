package ru.dvn.moviesearch.model

import ru.dvn.moviesearch.model.movie.Movie

class MockRepository : Repository {
    override fun getMoviesNowPlayingFromServer(): List<Movie> {
        return ArrayList<Movie>()
    }

    override fun getMoviesNowPlayingLocalStorage(): List<Movie> {
        return getFilledNowPlayingArray()
    }

    override fun getMoviesUpcomingFromServer(): List<Movie> {
        return ArrayList<Movie>()
    }

    override fun getMoviesUpcomingFromLocalStorage(): List<Movie> {
        return getFilledUpcomingArray()
    }

    private fun getFilledNowPlayingArray(): List<Movie> {
        return listOf(
            Movie(
                name = "Эмма",
                genre = "Комедия",
                filmLength = 124,
                rating = 6.9,
                budget = 10_000_000,
                revenue = 26_000_000,
                releaseDate = "13-02-2020",
                year = 2020,
            ),
            Movie(
                name = "Вивариум",
                genre = "Фантастика",
                filmLength = 97,
                rating = 6.1,
                budget = 4_000_000,
                revenue = 434_000,
                releaseDate = "18-05-2019",
                year = 2019,
                isFavorite = true
            ),
            Movie(
                name = "Воспоминания об убийстве",
                genre = "Детективы, Криминальные, Триллеры",
                filmLength = 120,
                rating = 8.1,
                budget = 2_800_000,
                revenue = 1_000_000,
                releaseDate = "no info",
                year = 2020
            ),
            Movie(
                name = "Почти знамениты",
                genre = "драма, комедия",
                filmLength = 112,
                rating = 6.7,
                budget = 0,
                revenue = 4_000_000,
                releaseDate = "06-09-2019",
                year = 2019
            ),
        )
    }

    private fun getFilledUpcomingArray(): List<Movie> {
        return listOf(
            Movie(
                name = "Первая ведьма",
                genre = "ужасы, фэнтези",
                filmLength = 95,
                rating = 5.8,
                budget = 0,
                revenue = 4_400_000,
                releaseDate = "19-07-2019",
                year = 2019
            ),
            Movie(
                name = "Приди ко мне",
                genre = "драма, триллер",
                filmLength = 112,
                rating = 5.2,
                budget = 0,
                revenue = 7_100,
                releaseDate = "06-09-2019",
                year = 2019
            ),
            Movie(
                name = "Почти знамениты",
                genre = "драма, комедия",
                filmLength = 112,
                rating = 6.7,
                budget = 0,
                revenue = 4_000_000,
                releaseDate = "06-09-2019",
                year = 2019
            ),
        )
    }
}