package com.example.skillcinemaapp.presentation.utility

sealed class LoadingState{

    object IsLoading: LoadingState()

    object LoadingIsFinished: LoadingState()

}
