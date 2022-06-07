package ru.dvn.moviesearch.model.movie.remote.detail.datasource

import retrofit2.Callback
import ru.dvn.moviesearch.BuildConfig
import ru.dvn.moviesearch.model.Retrofit
import ru.dvn.moviesearch.model.movie.remote.detail.MovieDetailDto
import ru.dvn.moviesearch.model.movie.remote.detail.remote.datasource.DetailsDataSource

class DetailsDataSourceImpl : DetailsDataSource {
    override fun getDetails(id: Long, callback: Callback<MovieDetailDto>) {
        Retrofit.getMovieApi()
            .getFilmDetail(BuildConfig.MOVIES_API_KEY, id)
            .enqueue(callback)
    }
}