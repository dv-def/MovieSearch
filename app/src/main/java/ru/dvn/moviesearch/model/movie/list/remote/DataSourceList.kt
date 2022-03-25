package ru.dvn.moviesearch.model.movie.list.remote

import com.google.gson.GsonBuilder
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.dvn.moviesearch.BuildConfig
import ru.dvn.moviesearch.model.movie.list.MovieList
import ru.dvn.moviesearch.model.movie.list.MoviesLoadMode

class DataSourceList {
    private val api = Retrofit.Builder()
        .baseUrl("https://kinopoiskapiunofficial.tech/")
        .addConverterFactory(GsonConverterFactory.create(
            GsonBuilder().setLenient().create()
        ))
        .build()
        .create(MovieListApi::class.java)


    fun getMovieList(mode: MoviesLoadMode, callback: Callback<MovieList>) {
        api.getMovieList(BuildConfig.MOVIES_API_KEY, mode.getMode()).enqueue(callback)
    }
}