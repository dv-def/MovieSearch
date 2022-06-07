package ru.dvn.moviesearch.model.movie.remote.list.repository

import retrofit2.Callback
import ru.dvn.moviesearch.model.movie.remote.list.datasource.DataSource
import ru.dvn.moviesearch.model.movie.remote.list.datasource.DataSourceListImpl
import ru.dvn.moviesearch.model.movie.remote.list.MovieListDto
import ru.dvn.moviesearch.model.movie.remote.list.TopParam

class MovieListRepositoryListImpl : MovieListRepository {
    private val dataSource: DataSource = DataSourceListImpl()

    override fun getMovieList(topParam: TopParam, callback: Callback<MovieListDto>) {
        dataSource.getMovieList(topParam, callback)
    }
}