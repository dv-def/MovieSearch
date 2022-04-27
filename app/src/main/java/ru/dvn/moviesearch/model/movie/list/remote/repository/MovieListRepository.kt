package ru.dvn.moviesearch.model.movie.list.remote.repository

import retrofit2.Callback
import ru.dvn.moviesearch.model.movie.list.remote.MovieListDto
import ru.dvn.moviesearch.model.movie.list.remote.TopParam

interface MovieListRepository {
    fun getMovieList(topParam: TopParam, callback: Callback<MovieListDto>)
}