package com.example.skillcinemaapp.domain

import com.example.skillcinemaapp.data.local.entities.*
import com.example.skillcinemaapp.data.repositories.RepositoryDataBase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UseCaseLocal @Inject constructor(private val repositoryDataBase: RepositoryDataBase) {

    fun getAllInterestingMovies(): Flow<List<Interesting>> {
        return repositoryDataBase.getAllInterestingMovies()
    }

    suspend fun addMovieToInteresting(interesting: Interesting) {
        return repositoryDataBase.addMovieToInteresting(interesting)
    }

    suspend fun cleanInteresting() {
        return repositoryDataBase.cleanInteresting()
    }

    fun getAllMoviesFromCustomCollection(): Flow<List<CustomCollection>> {
        return repositoryDataBase.getAllMoviesFromCustomCollection()
    }

    suspend fun addMovieToCustomCollection(customCollection: CustomCollection) {
        return repositoryDataBase.addMovieToCustomCollection(customCollection)
    }

    suspend fun deleteMovieFromCustomCollection(collectionName: String, movieId: Int) {
        return repositoryDataBase.deleteMovieFromCustomCollection(collectionName, movieId)
    }

    suspend fun deleteCustomCollection(collectionName: String) {
        return repositoryDataBase.deleteCustomCollection(collectionName)
    }

    fun getAllMovies(): Flow<List<Movie>> {
        return repositoryDataBase.getAllMovies()
    }

    suspend fun getMovieFromDataBaseById(movieId: Int): Movie {
        return repositoryDataBase.getMovieById(movieId)
    }

    suspend fun addMovie(movie: Movie) {
        return repositoryDataBase.addMovie(movie)
    }

    fun getAllWatched(): Flow<List<Watched>> {
        return repositoryDataBase.getAllWatched()
    }

    suspend fun addToWatched(watched: Watched) {
        return repositoryDataBase.addToWatched(watched)
    }

    suspend fun deleteFromWatched(movieId: Int) {
        return repositoryDataBase.deleteFromWatched(movieId)
    }

    suspend fun cleanWatched(){
        return repositoryDataBase.cleanWatched()
    }

    fun getAllFavorites(): Flow<List<Favorites>> {
        return repositoryDataBase.getAllFavorites()
    }

    suspend fun addToFavorites(favorites: Favorites) {
        return repositoryDataBase.addToFavorites(favorites)
    }

    suspend fun deleteFromFavorites(movieId: Int) {
        return repositoryDataBase.deleteFromFavorites(movieId)
    }

    fun getAllToWatch(): Flow<List<ToWatch>> {
        return repositoryDataBase.getAllToWatch()
    }

    suspend fun addToWatch(toWatch: ToWatch) {
        return repositoryDataBase.addToWatch(toWatch)
    }

    suspend fun deleteFromToWatch(movieId: Int) {
        return repositoryDataBase.deleteFromToWatch(movieId)
    }

}