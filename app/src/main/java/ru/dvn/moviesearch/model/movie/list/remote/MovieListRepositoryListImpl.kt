package ru.dvn.moviesearch.model.movie.list.remote

import retrofit2.Callback

class MovieListRepositoryListImpl : MovieListRepository {
    private val dataSource = DataSourceList()
    override fun getMovieList(mode: MoviesLoadMode, callback: Callback<MovieListDto>) {
        dataSource.getMovieList(mode, callback)
    }
}