package ru.dvn.moviesearch.model.movie.list.remote.datasource

import retrofit2.Callback
import ru.dvn.moviesearch.model.movie.list.remote.MovieListDto
import ru.dvn.moviesearch.model.movie.list.remote.TopParam

interface DataSource {
    fun getMovieList(topParam: TopParam, callback: Callback<MovieListDto>)
}