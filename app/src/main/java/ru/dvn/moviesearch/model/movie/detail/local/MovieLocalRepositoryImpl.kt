package ru.dvn.moviesearch.model.movie.detail.local

class MovieLocalRepositoryImpl(
    private val movieDao: MovieDao
) : MovieLocalRepository {

    override fun findByKinopoiskId(kinopoiskId: Long): MovieEntity? =
        movieDao.findByKinopoiskId(kinopoiskId)

    override fun save(movieEntity: MovieEntity) =
        movieDao.insert(movieEntity)

}