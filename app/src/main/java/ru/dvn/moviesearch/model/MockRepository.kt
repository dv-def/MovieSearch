package ru.dvn.moviesearch.model

import ru.dvn.moviesearch.model.movie.Movie

class MockRepository : Repository {
    override fun getMoviesFromServer(): List<Movie> {
        return ArrayList<Movie>()
    }

    override fun getMoviesFromLocalStorage(): List<Movie> {
        return getFilledArray()
    }

    private fun getFilledArray(): List<Movie> {
        return listOf(
            Movie("Эмма", "2020", 6.9, false),
            Movie("Вивариум", "2019", 5.7, true),
            Movie("Воспоминания об убийстве", "2020", 8.1, false),
            Movie("Почти знамениты", "2020", 7.2, false)
        )
    }
}