package ru.dvn.moviesearch.model

class MockRepository : Repository {
    override fun getMoviesFromServer(): List<Movie> {
        TODO("Not yet implemented")
    }

    override fun getMoviesFromLocalStorage(): List<Movie> {
        TODO("Not yet implemented")
    }
}