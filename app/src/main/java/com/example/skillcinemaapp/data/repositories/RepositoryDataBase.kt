package com.example.skillcinemaapp.data.repositories


import com.example.skillcinemaapp.data.local.dao.*
import com.example.skillcinemaapp.data.local.entities.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositoryDataBase @Inject constructor(
    private val toWatchDao: ToWatchDao,
    private val favoritesDao: FavoritesDao,
    private val watchedDao: WatchedDao,
    private val movieDao: MovieDao,
    private val customCollectionDao: CustomCollectionDao,
    private val interestingDao: InterestingDao
) {

    fun getAllInterestingMovies(): Flow<List<Interesting>> {
        return interestingDao.getAllInteresting()
    }

    suspend fun addMovieToInteresting(interesting: Interesting) {
        return interestingDao.insertToInteresting(interesting)
    }

    suspend fun cleanInteresting() {
        return interestingDao.cleanInteresting()
    }

    fun getAllMoviesFromCustomCollection(): Flow<List<CustomCollection>> {
        return customCollectionDao.getAllMoviesFromCustomCollection()
    }

    suspend fun addMovieToCustomCollection(customCollection: CustomCollection) {
        return customCollectionDao.addMovieToCustomCollection(customCollection)
    }

    suspend fun deleteMovieFromCustomCollection(collectionName: String, movieId: Int) {
        return customCollectionDao.deleteMovieFromCustomCollection(collectionName, movieId)
    }

    suspend fun deleteCustomCollection(collectionName: String) {
        return customCollectionDao.deleteCustomCollection(collectionName)
    }

    fun getAllMovies(): Flow<List<Movie>> {
        return movieDao.getAllMovies()
    }

    suspend fun addMovie(movie: Movie) {
        return movieDao.insertMovie(movie)
    }

    suspend fun deleteMovie(movieId: Int) {
        return movieDao.deleteFromMovies(movieId)
    }

    suspend fun getMovieById(movieId: Int): Movie {
        return movieDao.getMovieById(movieId)
    }

    fun getAllWatched(): Flow<List<Watched>> {
        return watchedDao.getAllWatched()
    }

    suspend fun addToWatched(watched: Watched) {
        return watchedDao.insertToWatched(watched)
    }

    suspend fun deleteFromWatched(movieId: Int) {
        return watchedDao.deleteFromWatched(movieId)
    }

    suspend fun cleanWatched() {
        return watchedDao.cleanWatched()
    }

    fun getAllFavorites(): Flow<List<Favorites>> {
        return favoritesDao.getAllFavorites()
    }

    suspend fun addToFavorites(favorites: Favorites) {
        return favoritesDao.insertToFavorites(favorites)
    }

    suspend fun deleteFromFavorites(movieId: Int) {
        return favoritesDao.deleteFromFavorites(movieId)
    }

    fun getAllToWatch(): Flow<List<ToWatch>> {
        return toWatchDao.getAllToWatch()
    }

    suspend fun addToWatch(toWatch: ToWatch) {
        return toWatchDao.insertToWatch(toWatch)
    }

    suspend fun deleteFromToWatch(movieId: Int) {
        return toWatchDao.deleteFromToWatch(movieId)
    }
}