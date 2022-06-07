package ru.dvn.moviesearch.model.movie.detail.remote

import retrofit2.Callback

interface DetailsRemoteRepository {
    fun getDetails(id: Long, callback: Callback<MovieDetailDto>)
}