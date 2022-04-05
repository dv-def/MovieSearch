package ru.dvn.moviesearch.model.movie.detail.local

interface MovieLocalRepository {
    fun findByKinopoiskId(kinopoiskId: Long): MovieEntity?
    fun save(movieEntity: MovieEntity)
}