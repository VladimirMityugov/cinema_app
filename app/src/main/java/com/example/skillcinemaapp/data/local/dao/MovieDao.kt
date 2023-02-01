package com.example.skillcinemaapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.skillcinemaapp.data.local.entities.Movie
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(vararg movie: Movie)

    @Query("SELECT * FROM Movie")
    fun getAllMovies(): Flow<List<Movie>>

    @Query("DELETE FROM Movie WHERE movieId = :movieId")
    suspend fun deleteFromMovies(movieId: Int)

    @Query("SELECT * FROM Movie WHERE movieId = :movieId")
    suspend fun getMovieById(movieId: Int): Movie



}