package ru.dvn.moviesearch.model.movie.remote.list.repository

import retrofit2.Callback
import ru.dvn.moviesearch.model.movie.remote.list.MovieListDto
import ru.dvn.moviesearch.model.movie.remote.list.TopParam

interface MovieListRepository {
    fun getMovieList(topParam: TopParam, callback: Callback<MovieListDto>)
}