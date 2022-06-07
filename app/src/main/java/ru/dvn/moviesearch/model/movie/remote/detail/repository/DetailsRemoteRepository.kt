package ru.dvn.moviesearch.model.movie.remote.detail.remote.repository

import retrofit2.Callback
import ru.dvn.moviesearch.model.movie.remote.detail.MovieDetailDto

interface DetailsRemoteRepository {
    fun getDetails(id: Long, callback: Callback<MovieDetailDto>)
}