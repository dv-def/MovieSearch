package ru.dvn.moviesearch.model.movie.list

import retrofit2.Callback
import ru.dvn.moviesearch.model.movie.list.remote.DataSourceList

class MovieListRepositoryListImpl : MovieListRepository {
    private val dataSource = DataSourceList()
    override fun getMovieList(mode: MoviesLoadMode, callback: Callback<MovieListDto>) {
        dataSource.getMovieList(mode, callback)
    }
}