package ru.dvn.moviesearch.model.movie.remote.detail.remote.datasource

import retrofit2.Callback
import ru.dvn.moviesearch.model.movie.remote.detail.MovieDetailDto

interface DetailsDataSource {
    fun getDetails(id: Long, callback: Callback<MovieDetailDto>)
}