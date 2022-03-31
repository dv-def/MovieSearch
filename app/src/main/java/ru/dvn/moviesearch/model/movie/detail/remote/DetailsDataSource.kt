package ru.dvn.moviesearch.model.movie.detail.remote

import retrofit2.Callback
import ru.dvn.moviesearch.BuildConfig
import ru.dvn.moviesearch.model.movie.Retrofit
import ru.dvn.moviesearch.model.movie.detail.MovieDetailDto

class DetailsDataSource {
    fun getDetails(id: Int, callback: Callback<MovieDetailDto>) {
        Retrofit.getDetailsApi()
            .getFilmDetail(BuildConfig.MOVIES_API_KEY, id)
            .enqueue(callback)
    }
}