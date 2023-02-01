package com.example.skillcinemaapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.skillcinemaapp.R
import com.example.skillcinemaapp.ui.onboarding.CollectionFragment
import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class OnboardingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_collection_layout, CollectionFragment.newInstance())
                .commitNow()
        }
    }
}