package com.example.skillcinemaapp.presentation.utility

sealed class Collections {
    object Favorites : Collections()
    object ToWatch : Collections()
    object Custom : Collections()
}
