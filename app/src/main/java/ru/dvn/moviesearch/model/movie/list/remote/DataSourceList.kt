package ru.dvn.moviesearch.model.movie.list.remote

import retrofit2.Callback
import ru.dvn.moviesearch.BuildConfig
import ru.dvn.moviesearch.model.movie.Retrofit

class DataSourceList {

    fun getMovieList(mode: MoviesLoadMode, callback: Callback<MovieListDto>) {
        Retrofit.getListApi()
            .getMovieList(BuildConfig.MOVIES_API_KEY, mode.getMode())
            .enqueue(callback)
    }
}