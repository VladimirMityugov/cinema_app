package com.example.skillcinemaapp.ui.search

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.skillcinemaapp.R
import com.example.skillcinemaapp.databinding.FragmentSearchSettingsBinding
import com.example.skillcinemaapp.presentation.MovieViewModel
import com.example.skillcinemaapp.presentation.utility.OrderTypes
import com.example.skillcinemaapp.presentation.utility.SearchTypes
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.slider.RangeSlider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlin.math.roundToInt

@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
class SearchSettingsFragment : Fragment() {

    private var _binding: FragmentSearchSettingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var backButton: AppCompatImageButton
    private lateinit var settingsTitle: AppCompatTextView
    private lateinit var country: AppCompatTextView
    private lateinit var genre: AppCompatTextView
    private lateinit var year: AppCompatTextView
    private lateinit var rating: AppCompatTextView
    private lateinit var slider: RangeSlider
    private lateinit var chipGroup: ChipGroup
    private lateinit var chipGroupSecond: ChipGroup
    private lateinit var watchedIcon: AppCompatImageView
    private lateinit var watchedFilter: AppCompatTextView
    private lateinit var all: Chip
    private lateinit var movies: Chip
    private lateinit var series: Chip
    private lateinit var date: Chip
    private lateinit var popularity: Chip
    private lateinit var ratingChip: Chip


    private val movieViewModel: MovieViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchSettingsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backButton = binding.backButton
        settingsTitle = binding.settingsTitle
        country = binding.country
        genre = binding.genre
        year = binding.year
        rating = binding.anyRating
        slider = binding.slider
        chipGroup = binding.chipGroup
        chipGroupSecond = binding.chipGroupSecond
        watchedIcon = binding.watchedIcon
        watchedFilter = binding.watchedFilterText
        all = binding.all
        movies = binding.movies
        series = binding.series
        date = binding.date
        popularity = binding.popularity
        ratingChip = binding.rating

        watchedFilter.setOnClickListener {
            onWatchedFilterClick()
        }

        watchedIcon.setOnClickListener {
            onWatchedFilterClick()
        }

        rating.setOnClickListener {
            setAnyRating()
        }

        country.setOnClickListener {
            findNavController().navigate(R.id.action_searchSettingsFragment_to_countrySelectFragment)
        }

        genre.setOnClickListener {
            findNavController().navigate(R.id.action_searchSettingsFragment_to_genreSelectFragment)
        }

        year.setOnClickListener {
            findNavController().navigate(R.id.action_searchSettingsFragment_to_yearSelectFragment)

        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.yearFromForSearch.collectLatest { yearFrom ->
                movieViewModel.yearToForSearch.collectLatest { yearTo ->
                    year.text = buildString {
                        append(requireActivity().getString(R.string.from))
                        append(" ")
                        append(yearFrom)
                        append(" ")
                        append(requireActivity().getString(R.string.to))
                        append(" ")
                        append(yearTo)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.countryForSearch.collectLatest { countryId ->
                country.text = movieViewModel.countries.value.find { it.id == countryId }?.country
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.genreForSearch.collectLatest { genreId ->
                genre.text =
                    movieViewModel.genres.value.find { it.id == genreId }?.genre?.replaceFirstChar { it -> it.uppercaseChar() }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.showWatchedAtSearchResult.collectLatest {
                if (it) {
                    watchedIcon.isActivated = true
                    watchedFilter.text = requireActivity().getString(R.string.watched)
                } else {
                    watchedIcon.isActivated = false
                    watchedFilter.text = requireActivity().getString(R.string.not_watched)
                }
            }
        }

        all.contentDescription = ALL
        movies.contentDescription = FILM
        series.contentDescription = TV_SERIES

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.searchType.collectLatest { searchType ->
                chipGroup.check(
                    when (searchType) {
                        SearchTypes.ALL -> R.id.all
                        SearchTypes.FILM -> R.id.movies
                        SearchTypes.TV_SERIES -> R.id.series
                    }
                )
            }
        }


        date.contentDescription = YEAR
        ratingChip.contentDescription = RATING
        popularity.contentDescription = NUM_VOTE

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.orderForSearch.collectLatest { orderType ->
                chipGroupSecond.check(
                    when (orderType) {
                        OrderTypes.YEAR -> R.id.date
                        OrderTypes.RATING -> R.id.rating
                        OrderTypes.NUM_VOTE -> R.id.popularity
                    }
                )
            }
        }


        chipGroup.children.forEach { chip ->
            (chip as Chip).setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    movieViewModel.onSearchTypeClick(buttonView.contentDescription as String)
                }
            }
        }

        chipGroupSecond.children.forEach { chip ->
            (chip as Chip).setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    movieViewModel.onSearchOrderClick(buttonView.contentDescription as String)
                }
            }
        }


        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.ratingFromForSearch.collectLatest { ratingFrom->
                movieViewModel.ratingToForSearch.collectLatest { ratingTo->
                    slider.setValues(ratingFrom.toFloat(),ratingTo.toFloat())
                }
            }
        }


        slider.addOnChangeListener(RangeSlider.OnChangeListener { slider, _, _ ->
            val values = slider.values
            movieViewModel.setRatingForSearch(values[0].roundToInt(), values[1].roundToInt())
        })

        slider.setMinSeparationValue(3.0F)

        slider.setLabelFormatter { it.toString() }



        backButton.setOnClickListener {
            findNavController().navigate(R.id.action_searchSettingsFragment_to_navigation_search)
        }
    }


    private fun onWatchedFilterClick() {
        movieViewModel.showWatchedMoviesAtSearchResult()
    }

    private fun setAnyRating() {
        slider.setValues(1.0F, 10.0F)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val RATING = "RATING"
        private const val NUM_VOTE = "NUM_VOTE"
        private const val YEAR = "YEAR"
        private const val ALL = "ALL"
        private const val FILM = "FILM"
        private const val TV_SERIES = "TV_SERIES"
    }
}