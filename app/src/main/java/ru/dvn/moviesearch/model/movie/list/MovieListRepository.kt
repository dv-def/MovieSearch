package ru.dvn.moviesearch.model.movie.list

import retrofit2.Callback

interface MovieListRepository {
    fun getMovieList(mode: MoviesLoadMode, callback: Callback<MovieListDto>)
}