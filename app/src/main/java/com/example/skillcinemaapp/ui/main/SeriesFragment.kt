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
import androidx.core.view.children
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.skillcinemaapp.data.remote.series_info_dto.Episode
import com.example.skillcinemaapp.databinding.FragmentSeriesBinding
import com.example.skillcinemaapp.presentation.MovieViewModel
import com.example.skillcinemaapp.presentation.adapters.*
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest



@AndroidEntryPoint
class SeriesFragment : Fragment() {

    private var _binding: FragmentSeriesBinding? = null
    private val binding get() = _binding!!

    private lateinit var backButton: AppCompatImageButton
    private lateinit var seriesTitle: AppCompatTextView
    private lateinit var seriesInfoRecycler: RecyclerView
    private lateinit var chipGroup: ChipGroup


    private val movieViewModel: MovieViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSeriesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backButton = binding.backButton
        seriesTitle = binding.seriesTitle
        seriesInfoRecycler = binding.seriesInfoRecyclerView
        chipGroup = binding.chipGroup

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.seriesInfo.collectLatest {
                if (it != null) {

                    val lastSeasonsNumber = it.items.lastOrNull()?.number
                    val firstSeasonNumber = it.items.firstOrNull()?.number
                    val totalSeasons = it.total
                    var episodeNumber = 0
                    it.items.forEach { series ->
                        episodeNumber += series.episodes.lastOrNull()?.episodeNumber!!
                    }

                    val seriesInfoAdapter = SeriesInfoAdapter(
                        seasonsNumber = totalSeasons,
                        episodeNumber = episodeNumber
                    )

                    if (firstSeasonNumber != null) {
                        for (i in firstSeasonNumber..lastSeasonsNumber!!) {
                            val chip = Chip(requireContext())
                            chip.text = "$i"
                            chip.id = i
                            chip.isClickable = true
                            chip.isCheckable = true
                            chipGroup.addView(chip)
                        }
                    }

                    seriesInfoRecycler.adapter = seriesInfoAdapter

                    val episodeList = mutableListOf<Episode>()
                    val firstEpisode = it.items.firstOrNull()?.episodes?.firstOrNull()
                    if (firstEpisode != null) {
                        episodeList.add(firstEpisode)
                    }
                    it.items.forEach { series ->
                        series.episodes.forEach { episode -> episodeList.add(episode) }
                    }
                    seriesInfoAdapter.submitList(episodeList)

                    chipGroup.children.forEach { chip ->
                        (chip as Chip).setOnCheckedChangeListener { buttonView, isChecked ->
                            if (isChecked) {
                                movieViewModel.getSeriesFilter(buttonView.id)
                                viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                                    movieViewModel.episodesBySeasonNumber.collectLatest { series ->
                                        val episodeListFiltered = mutableListOf<Episode>()
                                        val firstEpisodeFiltered =
                                            series.firstOrNull()?.episodes?.firstOrNull()
                                        if (firstEpisodeFiltered != null) {
                                            episodeListFiltered.add(firstEpisodeFiltered)
                                        }
                                        series.firstOrNull()?.episodes?.forEach { episode ->
                                            episodeListFiltered.add(episode)
                                        }
                                        seriesInfoAdapter.submitList(episodeListFiltered)
                                    }
                                }
                            } else {
                                seriesInfoAdapter.submitList(episodeList)
                            }
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.movieInfo.collectLatest {
                if (it != null) {
                    seriesTitle.text = it.nameRu ?: it.nameEn ?: it.nameOriginal ?: ""
                }
            }
        }

        backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
