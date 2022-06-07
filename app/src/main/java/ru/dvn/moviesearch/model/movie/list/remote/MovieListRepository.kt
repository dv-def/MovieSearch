package ru.dvn.moviesearch.model.movie.list.remote

import retrofit2.Callback

interface MovieListRepository {
    fun getMovieList(mode: MoviesLoadMode, callback: Callback<MovieListDto>)
}