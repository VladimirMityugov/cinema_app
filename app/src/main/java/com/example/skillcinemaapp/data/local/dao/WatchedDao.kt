package com.example.skillcinemaapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.skillcinemaapp.data.local.entities.Watched
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchedDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToWatched(vararg watched: Watched)

    @Query("SELECT * FROM Watched")
    fun getAllWatched(): Flow<List<Watched>>

    @Query("DELETE FROM Watched WHERE watchedId = :movieId")
    suspend fun deleteFromWatched(movieId: Int)

    @Query("DELETE FROM Watched")
    suspend fun cleanWatched()

}