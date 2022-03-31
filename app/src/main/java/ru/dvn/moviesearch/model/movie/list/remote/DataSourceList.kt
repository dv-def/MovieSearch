package ru.dvn.moviesearch.model.movie.list.remote

import retrofit2.Callback
import ru.dvn.moviesearch.BuildConfig
import ru.dvn.moviesearch.model.movie.Retrofit
import ru.dvn.moviesearch.model.movie.list.MovieListDto
import ru.dvn.moviesearch.model.movie.list.MoviesLoadMode

class DataSourceList {

    fun getMovieList(mode: MoviesLoadMode, callback: Callback<MovieListDto>) {
        Retrofit.getListApi()
            .getMovieList(BuildConfig.MOVIES_API_KEY, mode.getMode())
            .enqueue(callback)
    }
}