package ru.dvn.moviesearch.model.movie

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.dvn.moviesearch.model.movie.detail.remote.DetailsApi
import ru.dvn.moviesearch.model.movie.list.remote.MovieListApi

object Retrofit {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://kinopoiskapiunofficial.tech/")
        .addConverterFactory(
            GsonConverterFactory.create(
            GsonBuilder().setLenient().create()
        ))
        .build()

    fun getListApi(): MovieListApi =
        retrofit.create(MovieListApi::class.java)

    fun getDetailsApi(): DetailsApi =
        retrofit.create(DetailsApi::class.java)
}