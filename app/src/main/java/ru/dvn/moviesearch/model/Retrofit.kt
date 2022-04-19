package ru.dvn.moviesearch.model

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.dvn.moviesearch.model.movie.detail.remote.MovieDetailsApi
import ru.dvn.moviesearch.model.movie.list.remote.MovieListApi
import ru.dvn.moviesearch.model.staff.StaffApi

object Retrofit {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://kinopoiskapiunofficial.tech/")
        .addConverterFactory(
            GsonConverterFactory.create(
            GsonBuilder().setLenient().create()
        ))
        .build()

    fun getMovieListApi(): MovieListApi =
        retrofit.create(MovieListApi::class.java)

    fun getMovieDetailsApi(): MovieDetailsApi =
        retrofit.create(MovieDetailsApi::class.java)

    fun getStaffApi(): StaffApi =
        retrofit.create(StaffApi::class.java)
}