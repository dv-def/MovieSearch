package ru.dvn.moviesearch.model.movie.detail.remote

import com.google.gson.GsonBuilder
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.dvn.moviesearch.BuildConfig
import ru.dvn.moviesearch.model.movie.detail.MovieDetailDto

class DetailsDataSource {
    private val api = Retrofit.Builder()
        .baseUrl("https://kinopoiskapiunofficial.tech/")
        .addConverterFactory(
            GsonConverterFactory.create(
            GsonBuilder().setLenient().create()
        ))
        .build()
        .create(DetailsApi::class.java)

    fun getDetails(id: Int, callback: Callback<MovieDetailDto>) {
        api.getFilmDetail(BuildConfig.MOVIES_API_KEY, id).enqueue(callback)
    }
}