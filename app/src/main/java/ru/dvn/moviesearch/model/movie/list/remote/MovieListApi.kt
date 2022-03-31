package ru.dvn.moviesearch.model.movie.list.remote

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import ru.dvn.moviesearch.model.movie.list.MovieListDto

interface MovieListApi {
    @GET("api/v2.2/films/top")
    fun getMovieList(
        @Header("X-API-KEy") key: String,
        @Query("type") type: String
    ): Call<MovieListDto>
}