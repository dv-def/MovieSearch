package ru.dvn.moviesearch.model

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.dvn.moviesearch.model.movie.remote.api.MovieApi
import ru.dvn.moviesearch.model.staff.remote.api.StaffApi

object Retrofit {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://kinopoiskapiunofficial.tech/")
        .addConverterFactory(
            GsonConverterFactory.create(
            GsonBuilder().setLenient().create()
        ))
        .build()

    fun getMovieApi(): MovieApi =
        retrofit.create(MovieApi::class.java)

    fun getStaffApi(): StaffApi =
        retrofit.create(StaffApi::class.java)
}