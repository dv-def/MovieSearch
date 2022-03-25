package ru.dvn.moviesearch.model.movie.detail

import retrofit2.Callback

interface DetailsRepository {
    fun getDetails(id: Int, callback: Callback<MovieDetailDto>)
}