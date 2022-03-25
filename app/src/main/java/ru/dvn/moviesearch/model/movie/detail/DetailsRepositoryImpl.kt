package ru.dvn.moviesearch.model.movie.detail

import retrofit2.Callback
import ru.dvn.moviesearch.model.movie.detail.remote.DetailsDataSource

class DetailsRepositoryImpl : DetailsRepository {
    private val dataSource = DetailsDataSource()

    override fun getDetails(id: Int, callback: Callback<MovieDetailDto>) {
        dataSource.getDetails(id, callback)
    }
}