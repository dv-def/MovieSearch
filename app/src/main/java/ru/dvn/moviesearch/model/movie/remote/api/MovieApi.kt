package ru.dvn.moviesearch.model.movie.remote.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import ru.dvn.moviesearch.model.movie.remote.detail.MovieDetailDto
import ru.dvn.moviesearch.model.movie.remote.list.MovieListDto

interface MovieApi {
    @GET("api/v2.2/films/top")
    fun getMovieList(
        @Header("X-API-KEy") key: String,
        @Query("type") type: String
    ): Call<MovieListDto>

    @GET("api/v2.2/films/{id}")
    fun getFilmDetail(
        @Header("X-API-KEy") key: String,
        @Path("id") id: Long
    ): Call<MovieDetailDto>
}