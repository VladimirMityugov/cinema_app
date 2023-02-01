package com.example.skillcinemaapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.skillcinemaapp.data.local.entities.ToWatch
import kotlinx.coroutines.flow.Flow

@Dao
interface ToWatchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToWatch(vararg toWatch: ToWatch)

    @Query("SELECT * FROM ToWatch")
    fun getAllToWatch(): Flow<List<ToWatch>>

    @Query("DELETE FROM ToWatch WHERE toWatchId = :movieId")
    suspend fun deleteFromToWatch(movieId: Int)



}