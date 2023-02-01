package com.example.skillcinemaapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.skillcinemaapp.data.local.entities.Interesting
import kotlinx.coroutines.flow.Flow

@Dao
interface InterestingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToInteresting(vararg interesting: Interesting)

    @Query("SELECT * FROM Interesting")
    fun getAllInteresting(): Flow<List<Interesting>>

    @Query("DELETE FROM Interesting")
    suspend fun cleanInteresting()



}