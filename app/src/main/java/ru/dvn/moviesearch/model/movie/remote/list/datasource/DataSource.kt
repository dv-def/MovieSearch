package ru.dvn.moviesearch.model.movie.remote.list.datasource

import retrofit2.Callback
import ru.dvn.moviesearch.model.movie.remote.list.MovieListDto
import ru.dvn.moviesearch.model.movie.remote.list.TopParam

interface DataSource {
    fun getMovieList(topParam: TopParam, callback: Callback<MovieListDto>)
}