package com.example.skillcinemaapp.ui.main

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.view.children
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.skillcinemaapp.data.remote.images_dto.Image
import com.example.skillcinemaapp.databinding.FragmentImagesBinding
import com.example.skillcinemaapp.presentation.utility.ImageTypes
import com.example.skillcinemaapp.presentation.MovieViewModel
import com.example.skillcinemaapp.presentation.adapters.*
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ImagesFragment : Fragment() {

    private var _binding: FragmentImagesBinding? = null
    private val binding get() = _binding!!

    private lateinit var backButton: AppCompatImageButton
    private lateinit var chipGroup: ChipGroup
    private lateinit var imagesRecycler: RecyclerView


    private val imagesAdapter = GalleryAdapterIndividual(
        onImageClick = { image -> onImageClick(image) }
    )

    private val movieViewModel: MovieViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImagesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backButton = binding.backButton
        imagesRecycler = binding.imagesRecyclerView
        imagesRecycler.adapter = imagesAdapter
        chipGroup = binding.chipGroup

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.getImages(null).collectLatest {
                imagesAdapter.submitData(it)
            }
        }


        for (types in ImageTypes.values()) {
            val chip = Chip(requireContext())
            chip.contentDescription = types.name
            val typeName = types.name
            chip.text = when (typeName) {
                "STILL" -> STILL
                "SHOOTING" -> SHOOTING
                "POSTER" -> POSTER
                "FAN_ART" -> FAN_ART
                "PROMO" -> PROMO
                "CONCEPT" -> CONCEPT
                "WALLPAPER" -> WALLPAPER
                "COVER" -> COVER
                "SCREENSHOT" -> SCREENSHOT
                else -> ""
            }
            chip.id = types.ordinal
            chip.isClickable = true
            chip.isCheckable = true
            chipGroup.addView(chip)
        }

        chipGroup.children.forEach { chip ->
            (chip as Chip).setOnCheckedChangeListener { buttonView, isChecked ->

                if (isChecked) {
                    viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                        movieViewModel.getImages(buttonView.contentDescription as String?)
                            .collectLatest {
                                imagesAdapter.submitData(it)
                            }
                    }
                } else {
                    viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                        movieViewModel.getImages(null).collectLatest {
                            imagesAdapter.submitData(it)
                        }
                    }
                }
            }
        }

        backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun onImageClick(image: Image) {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val STILL = "Кадры"
        private const val SHOOTING = "Изображения со съемок"
        private const val POSTER = "Постеры"
        private const val FAN_ART = "Фан-арты"
        private const val PROMO = "Промо"
        private const val CONCEPT = "Kонцепт-арты"
        private const val WALLPAPER = "Обои"
        private const val COVER = "Обложки"
        private const val SCREENSHOT = "Скриншоты"
    }
}
