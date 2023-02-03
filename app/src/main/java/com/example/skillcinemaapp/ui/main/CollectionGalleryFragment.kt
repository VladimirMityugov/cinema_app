package com.example.skillcinemaapp.ui.main

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.*
import com.example.skillcinemaapp.R
import com.example.skillcinemaapp.databinding.FragmentGalleryCollectionBinding
import com.example.skillcinemaapp.presentation.MovieViewModel
import com.example.skillcinemaapp.presentation.adapters.FragmentGalleryCollectionAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest



private const val TAG = "COLLECTION"

@AndroidEntryPoint
class CollectionGalleryFragment : Fragment() {

    private var _binding: FragmentGalleryCollectionBinding? = null
    private val binding get() = _binding!!

    private lateinit var galleryCollectionAdapter: FragmentGalleryCollectionAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var skipButton: AppCompatImageButton
    private lateinit var movieTitle: AppCompatTextView


    private val movieViewModel: MovieViewModel by activityViewModels()


    private val pagerCallBack = object : OnPageChangeCallback() {

        var currentPage = 0
        var isSettled = false

        override fun onPageScrollStateChanged(state: Int) {
            super.onPageScrollStateChanged(state)
            if (state == SCROLL_STATE_DRAGGING) {
                isSettled = false
            }
            if (state == SCROLL_STATE_SETTLING) {
                isSettled = true
            }
            if (state == SCROLL_STATE_IDLE && !isSettled) {
                findNavController().navigate(R.id.action_collectionGalleryFragment_to_movieDetailsFragment)
            }
        }

        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            currentPage = position + 1
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryCollectionBinding.inflate(layoutInflater)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        viewPager = binding.pager
        skipButton = binding.skipOnboarding
        movieTitle = binding.movieTitle
        viewPager.orientation = ORIENTATION_HORIZONTAL

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.images.collectLatest { images ->
                movieViewModel.imageSelected.collectLatest { imageSelected ->
                    if (images != null) {
                        galleryCollectionAdapter = FragmentGalleryCollectionAdapter(
                            this@CollectionGalleryFragment,
                            images.items.size
                        )
                        viewPager.adapter = galleryCollectionAdapter
                        val imageToDisplay =
                            images.items.find { it.imageUrl == imageSelected?.imageUrl }
                        val pageToDisplay = images.items.indexOf(imageToDisplay)
                        viewPager.setCurrentItem(pageToDisplay, false)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.movieInfo.collectLatest { movieFromApi ->
                movieViewModel.movieById.collectLatest { movieFromDB ->
                    if (movieFromDB != null) {
                        movieTitle.text = movieFromDB.movieName ?: movieFromDB.nameEn ?: ""
                    } else {
                        if (movieFromApi != null) {
                            movieTitle.text = movieFromApi.nameRu ?: movieFromApi.nameEn
                                    ?: movieFromApi.nameOriginal ?: ""
                        }
                    }
                }
            }
        }

        skipButton.setOnClickListener {
            findNavController().navigate(R.id.action_collectionGalleryFragment_to_movieDetailsFragment)
        }

        viewPager.registerOnPageChangeCallback(pagerCallBack)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
