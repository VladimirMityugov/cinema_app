package com.example.skillcinemaapp.ui.main

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.skillcinemaapp.R
import com.example.skillcinemaapp.databinding.FragmentGallerySingleBinding
import com.example.skillcinemaapp.presentation.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest


@AndroidEntryPoint
class GallerySingleFragment : Fragment() {

    private var _binding: FragmentGallerySingleBinding? = null
    private val binding get() = _binding!!

    private lateinit var galleryPicture: AppCompatImageView

    private val movieViewModel: MovieViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGallerySingleBinding.inflate(layoutInflater)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val position = arguments?.getInt(POSITION)

        galleryPicture = binding.galleryPicture

        galleryPicture.setOnClickListener {
            findNavController().navigate(R.id.action_collectionGalleryFragment_to_movieDetailsFragment)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.images.collectLatest {
                Glide
                    .with(galleryPicture.context)
                    .load(it!!.items[position!!].imageUrl)
                    .into(galleryPicture)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        var POSITION = "position"

        @JvmStatic
        fun newInstance(position: Int) = GallerySingleFragment().apply {
            arguments = Bundle().apply {
                putInt(POSITION, position)
            }
        }
    }
}
