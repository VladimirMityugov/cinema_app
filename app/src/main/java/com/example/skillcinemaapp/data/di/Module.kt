package com.example.skillcinemaapp.data.di

import android.app.Application
import androidx.room.Room
import com.example.skillcinemaapp.data.local.MovieDataBase
import com.example.skillcinemaapp.data.remote.MovieApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


@Module
@InstallIn(ViewModelComponent::class)
object Module {

    @Provides
    @ViewModelScoped
    fun provideMovieDatabase(app: Application): MovieDataBase {
        return Room.databaseBuilder(
            app.applicationContext,
            MovieDataBase::class.java,
            "movieDataBase"
        ).build()
    }

    @Provides
    @ViewModelScoped
    fun provideFavoritesDao(dataBase: MovieDataBase) = dataBase.favoritesDao()

    @Provides
    @ViewModelScoped
    fun provideToWatchDao(dataBase: MovieDataBase) = dataBase.toWatchDao()

    @Provides
    @ViewModelScoped
    fun provideWatchedDao(dataBase: MovieDataBase) = dataBase.watchedDao()

    @Provides
    @ViewModelScoped
    fun provideMovieDao(dataBase: MovieDataBase) = dataBase.movieDao()

    @Provides
    @ViewModelScoped
    fun provideInterestingDao(dataBase: MovieDataBase) = dataBase.interestingDao()

    @Provides
    @ViewModelScoped
    fun provideCustomSelectionDao(dataBase: MovieDataBase) = dataBase.customCollectionDao()

    @Provides
    @ViewModelScoped
    fun provideMovieApi(): MovieApi {

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        return Retrofit.Builder()
            .baseUrl(MovieApi.BASE_URl)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(MovieApi::class.java)
    }
}