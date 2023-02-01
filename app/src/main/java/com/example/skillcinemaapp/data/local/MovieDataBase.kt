package com.example.skillcinemaapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.skillcinemaapp.data.local.dao.*
import com.example.skillcinemaapp.data.local.entities.*


@Database(
    entities = [Favorites::class, ToWatch::class, Watched::class, Movie::class, CustomCollection::class, Interesting::class],
    version = 1
)
abstract class MovieDataBase : RoomDatabase() {
    abstract fun favoritesDao(): FavoritesDao
    abstract fun toWatchDao(): ToWatchDao
    abstract fun watchedDao(): WatchedDao
    abstract fun movieDao(): MovieDao
    abstract fun customCollectionDao(): CustomCollectionDao
    abstract fun interestingDao(): InterestingDao
}