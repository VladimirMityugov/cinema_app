package com.example.skillcinemaapp.presentation.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.skillcinemaapp.ui.main.GallerySingleFragment


class FragmentGalleryCollectionAdapter (
    fragment: Fragment,
    private val gallerySize:Int
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = gallerySize

    override fun createFragment(position: Int): Fragment = GallerySingleFragment.newInstance(position)

}