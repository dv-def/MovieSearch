package ru.dvn.moviesearch.model.movie.detail.remote

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import ru.dvn.moviesearch.model.movie.detail.MovieDetailDto
import ru.dvn.moviesearch.model.movie.list.FilmDTO

interface DetailsApi {
    @GET("api/v2.2/films/{id}")
    fun getFilmDetail(
        @Header("X-API-KEy") key: String,
        @Path("id") id: Int
    ): Call<MovieDetailDto>
}