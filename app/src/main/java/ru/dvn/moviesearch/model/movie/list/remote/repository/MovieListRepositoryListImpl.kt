package ru.dvn.moviesearch.model.movie.list.remote.repository

import retrofit2.Callback
import ru.dvn.moviesearch.model.movie.list.remote.datasource.DataSource
import ru.dvn.moviesearch.model.movie.list.remote.datasource.DataSourceListImpl
import ru.dvn.moviesearch.model.movie.list.remote.MovieListDto
import ru.dvn.moviesearch.model.movie.list.remote.TopParam

class MovieListRepositoryListImpl : MovieListRepository {
    private val dataSource: DataSource = DataSourceListImpl()

    override fun getMovieList(topParam: TopParam, callback: Callback<MovieListDto>) {
        dataSource.getMovieList(topParam, callback)
    }
}