package ru.dvn.moviesearch.model.movie.detail.remote

import retrofit2.Callback
import ru.dvn.moviesearch.BuildConfig
import ru.dvn.moviesearch.model.movie.Retrofit

class DetailsDataSource {
    fun getDetails(id: Long, callback: Callback<MovieDetailDto>) {
        Retrofit.getDetailsApi()
            .getFilmDetail(BuildConfig.MOVIES_API_KEY, id)
            .enqueue(callback)
    }
}