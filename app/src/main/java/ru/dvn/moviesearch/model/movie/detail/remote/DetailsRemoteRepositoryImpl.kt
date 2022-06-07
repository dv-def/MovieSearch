package ru.dvn.moviesearch.model.movie.detail.remote

import retrofit2.Callback

class DetailsRemoteRepositoryImpl : DetailsRemoteRepository {
    private val dataSource = DetailsDataSource()

    override fun getDetails(id: Long, callback: Callback<MovieDetailDto>) {
        dataSource.getDetails(id, callback)
    }
}