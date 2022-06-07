package ru.dvn.moviesearch.model.movie.remote.list.datasource

import retrofit2.Callback
import ru.dvn.moviesearch.BuildConfig
import ru.dvn.moviesearch.model.Retrofit
import ru.dvn.moviesearch.model.movie.remote.list.MovieListDto
import ru.dvn.moviesearch.model.movie.remote.list.TopParam

class DataSourceListImpl : DataSource {
    override fun getMovieList(topParam: TopParam, callback: Callback<MovieListDto>) {
        Retrofit.getMovieApi()
            .getMovieList(BuildConfig.MOVIES_API_KEY, topParam.paramName)
            .enqueue(callback)
    }
}