package ru.dvn.moviesearch.model.movie.remote.detail.remote.repository

import retrofit2.Callback
import ru.dvn.moviesearch.model.movie.remote.detail.MovieDetailDto
import ru.dvn.moviesearch.model.movie.remote.detail.datasource.DetailsDataSourceImpl

class DetailsRemoteRepositoryImpl : DetailsRemoteRepository {
    private val dataSource = DetailsDataSourceImpl()

    override fun getDetails(id: Long, callback: Callback<MovieDetailDto>) {
        dataSource.getDetails(id, callback)
    }
}