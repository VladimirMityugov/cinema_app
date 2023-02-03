package com.example.skillcinemaapp.ui.main

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.skillcinemaapp.R
import com.example.skillcinemaapp.data.remote.custom_selection_dto.ItemCustom
import com.example.skillcinemaapp.data.remote.popular_dto.Film
import com.example.skillcinemaapp.data.remote.premiers_dto.Item
import com.example.skillcinemaapp.databinding.FragmentMainBinding
import com.example.skillcinemaapp.presentation.*
import com.example.skillcinemaapp.presentation.adapters.*
import com.example.skillcinemaapp.presentation.utility.LoadingState
import com.example.skillcinemaapp.presentation.utility.Selections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import java.util.*

private const val TAG = "MAIN_FRAGMENT"

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var premiers: RecyclerView
    private lateinit var popular: RecyclerView
    private lateinit var top: RecyclerView
    private lateinit var firstCustom: RecyclerView
    private lateinit var secondCustom: RecyclerView
    private lateinit var series: RecyclerView
    private lateinit var showAllPremiers: AppCompatButton
    private lateinit var showAllPopular: AppCompatButton
    private lateinit var showAllTop: AppCompatButton
    private lateinit var showAllFirstCustom: AppCompatButton
    private lateinit var showAllSecondCustom: AppCompatButton
    private lateinit var showAllSeries: AppCompatButton
    private lateinit var firstCustomSelectionGenre: AppCompatTextView
    private lateinit var firstCustomSelectionCountry: AppCompatTextView
    private lateinit var secondCustomSelectionGenre: AppCompatTextView
    private lateinit var secondCustomSelectionCountry: AppCompatTextView
    private lateinit var loader: ProgressBar
    private lateinit var premiersTitle: AppCompatTextView
    private lateinit var popularMoviesTitle: AppCompatTextView
    private lateinit var top250MoviesTitle: AppCompatTextView
    private lateinit var seriesTitle: AppCompatTextView

    private val premiersAdapter = PremiersAdapterCommon(
        onPremiersItemClick = { Item -> onItemClickPremiersSelection(Item) },
        onShowAllPremiersClick = { onItemClickShowPremiers() }
    )

    private val popularAdapter = PopularAdapterCommon(
        onPopularItemClick = { Film -> onItemClickPopularSelection(Film) },
        onShowAllPopularClick = { onItemClickShowPopular() }
    )

    private val topAdapter = TopAdapterCommon(
        onTopItemClick = { Film -> onItemClickTopSelection(Film) },
        onShowAllTopClick = { onItemClickShowTop() }
    )

    private val firstCustomAdapter = FirstCustomAdapterCommon(
        onFirstCustomItemClick = { ItemCustom -> onItemClickFirstCustomSelection(ItemCustom) },
        onShowAllFirstCustomClick = { onItemClickShowFirstCustom() }
    )

    private val secondCustomAdapter = SecondCustomAdapterCommon(
        onSecondCustomItemClick = { ItemCustom -> onItemClickSecondCustomSelection(ItemCustom) },
        onShowAllSecondCustomClick = { onItemClickShowSecondCustom() }
    )

    private val seriesAdapter = SeriesAdapterCommon(
        onSeriesItemClick = { ItemCustom -> onItemClickSeriesSelection(ItemCustom) },
        onShowAllSeriesClick = { onItemClickShowSeries() }
    )

    private val movieViewModel: MovieViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        premiers = binding.premiersRecyclerView
        popular = binding.popularMoviesRecyclerView
        top = binding.top250MoviesRecyclerView
        firstCustom = binding.firstRandomRecyclerView
        secondCustom = binding.secondRandomRecyclerView
        series = binding.seriesRecyclerView
        premiersTitle = binding.premiersTitle
        popularMoviesTitle = binding.popularMoviesTitle
        top250MoviesTitle = binding.top250MoviesTitle
        seriesTitle = binding.seriesTitle

        showAllPremiers = binding.showAllPremiers
        showAllPopular = binding.showAllPopular
        showAllTop = binding.showAllTop250
        showAllFirstCustom = binding.showAllFirstRandomSelection
        showAllSecondCustom = binding.showAllSecondRandomSelection
        showAllSeries = binding.showAllSeries

        loader = binding.loader

        premiers.adapter = premiersAdapter
        popular.adapter = popularAdapter
        top.adapter = topAdapter
        firstCustom.adapter = firstCustomAdapter
        secondCustom.adapter = secondCustomAdapter
        series.adapter = seriesAdapter

        firstCustomSelectionGenre = binding.firstRandomSelectionGenre
        secondCustomSelectionGenre = binding.secondRandomSelectionGenre
        firstCustomSelectionCountry = binding.firstRandomSelectionCountry
        secondCustomSelectionCountry = binding.secondRandomSelectionCountry

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.loadingState.collectLatest {
                when (it) {
                    LoadingState.IsLoading -> {
                        loader.isVisible = true
                    }
                    LoadingState.LoadingIsFinished -> {
                        loader.isVisible = false
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.genreValueFirst.collectLatest { genre ->
                movieViewModel.countryValueFirst.collectLatest { country ->
                    if (genre != null && country != null) {
                        movieViewModel.getFirstListSelection(country, genre)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.genreValueSecond.collectLatest { genre ->
                movieViewModel.countryValueSecond.collectLatest { country ->
                    if (genre != null && country != null) {
                        movieViewModel.getSecondListSelection(country, genre)
                    }
                }
            }
        }



        showAllPremiers.setOnClickListener {
            onItemClickShowPremiers()
        }

        showAllTop.setOnClickListener {
            onItemClickShowTop()
        }

        showAllSeries.setOnClickListener {
            onItemClickShowSeries()
        }

        showAllPopular.setOnClickListener {
            onItemClickShowPopular()
        }

        showAllFirstCustom.setOnClickListener {
            onItemClickShowFirstCustom()
        }

        showAllSecondCustom.setOnClickListener {
            onItemClickShowSecondCustom()
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.countryValueFirst.collectLatest { id ->
                movieViewModel.countries.collectLatest {
                    if (it.isNotEmpty() && id != null) {
                        firstCustomSelectionCountry.visibility = View.GONE
                        firstCustomSelectionCountry.text = it[id - 1].country
                    } else {
                        firstCustomSelectionCountry.visibility = View.GONE
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.genreValueFirst.collectLatest { id ->
                movieViewModel.genres.collectLatest {
                    if (it.isNotEmpty() && id != null) {
                        firstCustomSelectionGenre.visibility = View.GONE
                        firstCustomSelectionGenre.text =
                            it[id - 1].genre?.replaceFirstChar { Char ->
                                if (Char.isLowerCase()) Char.titlecase(
                                    Locale.ROOT
                                ) else it.toString()
                            }
                    } else {
                        firstCustomSelectionGenre.visibility = View.GONE
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.countryValueSecond.collectLatest { id ->
                movieViewModel.countries.collectLatest {
                    if (it.isNotEmpty() && id != null) {
                        secondCustomSelectionCountry.visibility = View.GONE
                        secondCustomSelectionCountry.text = it[id - 1].country
                    } else {
                        secondCustomSelectionCountry.visibility = View.GONE
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.genreValueSecond.collectLatest { id ->
                movieViewModel.genres.collectLatest {
                    if (it.isNotEmpty() && id != null) {
                        secondCustomSelectionGenre.visibility = View.GONE
                        secondCustomSelectionGenre.text =
                            it[id - 1].genre?.replaceFirstChar { Char ->
                                if (Char.isLowerCase()) Char.titlecase(
                                    Locale.ROOT
                                ) else it.toString()
                            }
                    } else {
                        secondCustomSelectionGenre.visibility = View.GONE
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.premiers.collectLatest {
                if (it.isNotEmpty()) {
                    premiersTitle.visibility = View.VISIBLE
                    premiers.visibility = View.VISIBLE
                    if (it.size >= 21) {
                        showAllPremiers.isVisible = true
                        premiersAdapter.submitList(it.take(21))
                    } else premiersAdapter.submitList(it)
                } else {
                    premiers.visibility = View.INVISIBLE
                    premiersTitle.visibility = View.GONE
                }
            }
        }


        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.premiers.collectLatest {
                if (it.isNotEmpty()) {
                    movieViewModel.updatePremiersWatchStatus()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.popular.collectLatest {
                if (it.isNotEmpty()) {
                    popularMoviesTitle.visibility = View.VISIBLE
                    popular.visibility = View.VISIBLE
                    if (it.size >= 21) {
                        showAllPopular.isVisible = true
                        popularAdapter.submitList(it.take(21))
                    } else popularAdapter.submitList(it)
                } else {
                    popular.visibility = View.INVISIBLE
                    popularMoviesTitle.visibility = View.GONE
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.popular.collectLatest {
                if (it.isNotEmpty()) {
                    movieViewModel.updatePopularWatchStatus()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.top.collectLatest {
                if (it.isNotEmpty()) {
                    top250MoviesTitle.visibility = View.VISIBLE
                    top.visibility = View.VISIBLE
                    if (it.size >= 21) {
                        showAllTop.isVisible = true
                        topAdapter.submitList(it.take(21))
                    } else topAdapter.submitList(it)
                } else {
                    top.visibility = View.INVISIBLE
                    top250MoviesTitle.visibility = View.GONE
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.top.collectLatest {
                if (it.isNotEmpty()) {
                    movieViewModel.updateTopWatchStatus()
                }
            }
        }


        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.series.collectLatest {
                if (it.isNotEmpty()) {
                    seriesTitle.visibility = View.VISIBLE
                    series.visibility = View.VISIBLE
                    if (it.size >= 21) {
                        showAllSeries.isVisible = true
                        seriesAdapter.submitList(it.take(21))
                    } else seriesAdapter.submitList(it)
                } else {
                    series.visibility = View.INVISIBLE
                    seriesTitle.visibility = View.GONE
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.series.collectLatest {
                if (it.isNotEmpty()) {
                    movieViewModel.updateSeriesWatchStatus()
                }
            }
        }


        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.firstCustom.collectLatest {
                if (it.isNotEmpty()) {
                    firstCustom.visibility = View.VISIBLE
                    firstCustomSelectionCountry.visibility = View.VISIBLE
                    firstCustomSelectionGenre.visibility = View.VISIBLE
                    if (it.size >= 21) {
                        showAllFirstCustom.isVisible = true
                        firstCustomAdapter.submitList(it.take(21))
                    } else {
                        firstCustomAdapter.submitList(it)
                    }
                } else {
                    firstCustom.visibility = View.INVISIBLE
                    firstCustomSelectionCountry.visibility = View.GONE
                    firstCustomSelectionGenre.visibility = View.GONE
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.firstCustom.collectLatest {
                if (it.isNotEmpty()) {
                    movieViewModel.updateFirstCustomWatchStatus()
                }
            }
        }


        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.secondCustom.collectLatest {
                if (it.isNotEmpty()) {
                    secondCustom.visibility = View.VISIBLE
                    secondCustomSelectionCountry.visibility = View.VISIBLE
                    secondCustomSelectionGenre.visibility = View.VISIBLE
                    if (it.size >= 21) {
                        showAllSecondCustom.isVisible = true
                        secondCustomAdapter.submitList(it.take(21))
                    } else secondCustomAdapter.submitList(it)
                } else {
                    secondCustom.visibility = View.GONE
                    secondCustomSelectionCountry.visibility = View.GONE
                    secondCustomSelectionGenre.visibility = View.GONE
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.secondCustom.collectLatest {
                if (it.isNotEmpty()) {
                    movieViewModel.updateSecondCustomWatchStatus()
                }
            }
        }


        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.premiers.collectLatest { premiers ->
                movieViewModel.firstCustom.collectLatest { firstCustom ->
                    movieViewModel.secondCustom.collectLatest { secondCustom ->
                        movieViewModel.top.collectLatest { top ->
                            movieViewModel.popular.collectLatest { popular ->
                                movieViewModel.series.collectLatest { series ->
                                    if (premiers.isNotEmpty()
                                        && firstCustom.isNotEmpty()
                                        && secondCustom.isNotEmpty() || secondCustom.isEmpty()
                                        && top.isNotEmpty()
                                        && popular.isNotEmpty()
                                        && series.isNotEmpty()
                                    ) {
                                        movieViewModel.stopLoading()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun onItemClickFirstCustomSelection(item: ItemCustom) {
        movieViewModel.onAddMovieToDataBase(item.kinopoiskId)
        movieViewModel.movieSelected(item.kinopoiskId)
        movieViewModel.getImagesList(item.kinopoiskId)
        movieViewModel.getStaffInfo(item.kinopoiskId)
        movieViewModel.getActorsInfo(item.kinopoiskId)
        movieViewModel.getSimilarMovies(item.kinopoiskId)
        movieViewModel.getSeriesInfo(item.kinopoiskId)
        movieViewModel.chooseSelection(Selections.FirstCustom)
        movieViewModel.onInterestingButtonClick(item.kinopoiskId)
        movieViewModel.getMovieFromDataBaseById(item.kinopoiskId)
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.movieById.collectLatest {
                if (it != null && it.movieId == item.kinopoiskId) {
                    Log.d(TAG, "This movie is in DB")
                } else {
                    movieViewModel.getMovieInfo(item.kinopoiskId)
                }
            }
        }
        findNavController().navigate(R.id.action_navigation_main_to_movieDetailsFragment)
    }

    private fun onItemClickSecondCustomSelection(item: ItemCustom) {
        movieViewModel.onAddMovieToDataBase(item.kinopoiskId)
        movieViewModel.movieSelected(item.kinopoiskId)
        movieViewModel.getImagesList(item.kinopoiskId)
        movieViewModel.getStaffInfo(item.kinopoiskId)
        movieViewModel.getActorsInfo(item.kinopoiskId)
        movieViewModel.getSimilarMovies(item.kinopoiskId)
        movieViewModel.getSeriesInfo(item.kinopoiskId)
        movieViewModel.chooseSelection(Selections.SecondCustom)
        movieViewModel.onInterestingButtonClick(item.kinopoiskId)
        movieViewModel.getMovieFromDataBaseById(item.kinopoiskId)
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.movieById.collectLatest {
                if (it != null && it.movieId == item.kinopoiskId) {
                    Log.d(TAG, "This movie is in DB")
                } else {
                    Log.d(TAG, "This movie is not in DB")
                    movieViewModel.getMovieInfo(item.kinopoiskId)
                }
            }
        }
        findNavController().navigate(R.id.action_navigation_main_to_movieDetailsFragment)
    }

    private fun onItemClickSeriesSelection(item: ItemCustom) {
        movieViewModel.onAddMovieToDataBase(item.kinopoiskId)
        movieViewModel.movieSelected(item.kinopoiskId)
        movieViewModel.getImagesList(item.kinopoiskId)
        movieViewModel.getStaffInfo(item.kinopoiskId)
        movieViewModel.getActorsInfo(item.kinopoiskId)
        movieViewModel.getSimilarMovies(item.kinopoiskId)
        movieViewModel.getMovieInfo(item.kinopoiskId)
        movieViewModel.getSeriesInfo(item.kinopoiskId)
        movieViewModel.chooseSelection(Selections.Series)
        movieViewModel.onInterestingButtonClick(item.kinopoiskId)
        findNavController().navigate(R.id.action_navigation_main_to_movieDetailsFragment)
    }

    private fun onItemClickTopSelection(item: Film) {
        movieViewModel.onAddMovieToDataBase(item.filmId)
        movieViewModel.movieSelected(item.filmId)
        movieViewModel.getImagesList(item.filmId)
        movieViewModel.getStaffInfo(item.filmId)
        movieViewModel.getActorsInfo(item.filmId)
        movieViewModel.getMovieInfo(item.filmId)
        movieViewModel.getSimilarMovies(item.filmId)
        movieViewModel.getSeriesInfo(item.filmId)
        movieViewModel.chooseSelection(Selections.Top)
        movieViewModel.onInterestingButtonClick(item.filmId)
        findNavController().navigate(R.id.action_navigation_main_to_movieDetailsFragment)
    }

    private fun onItemClickPopularSelection(item: Film) {
        movieViewModel.onAddMovieToDataBase(item.filmId)
        movieViewModel.movieSelected(item.filmId)
        movieViewModel.getImagesList(item.filmId)
        movieViewModel.getStaffInfo(item.filmId)
        movieViewModel.getActorsInfo(item.filmId)
        movieViewModel.getSimilarMovies(item.filmId)
        movieViewModel.getMovieInfo(item.filmId)
        movieViewModel.getSeriesInfo(item.filmId)
        movieViewModel.chooseSelection(Selections.Popular)
        movieViewModel.onInterestingButtonClick(item.filmId)
        findNavController().navigate(R.id.action_navigation_main_to_movieDetailsFragment)
    }

    private fun onItemClickPremiersSelection(item: Item) {
        movieViewModel.onAddMovieToDataBase(item.kinopoiskId)
        movieViewModel.movieSelected(item.kinopoiskId)
        movieViewModel.getImagesList(item.kinopoiskId)
        movieViewModel.getStaffInfo(item.kinopoiskId)
        movieViewModel.getActorsInfo(item.kinopoiskId)
        movieViewModel.getSimilarMovies(item.kinopoiskId)
        movieViewModel.getMovieInfo(item.kinopoiskId)
        movieViewModel.getSeriesInfo(item.kinopoiskId)
        movieViewModel.chooseSelection(Selections.Premiers)
        movieViewModel.onInterestingButtonClick(item.kinopoiskId)
        findNavController().navigate(R.id.action_navigation_main_to_movieDetailsFragment)
    }

    private fun onItemClickShowPremiers() {
        movieViewModel.chooseSelection(Selections.Premiers)
        findNavController().navigate(R.id.action_navigation_main_to_selectionFragment)
    }

    private fun onItemClickShowTop() {
        movieViewModel.chooseSelection(Selections.Top)
        findNavController().navigate(R.id.action_navigation_main_to_selectionFragment)
    }

    private fun onItemClickShowPopular() {
        movieViewModel.chooseSelection(Selections.Popular)
        findNavController().navigate(R.id.action_navigation_main_to_selectionFragment)
    }

    private fun onItemClickShowSeries() {
        movieViewModel.chooseSelection(Selections.Series)
        findNavController().navigate(R.id.action_navigation_main_to_selectionFragment)
    }

    private fun onItemClickShowFirstCustom() {
        movieViewModel.chooseSelection(Selections.FirstCustom)
        findNavController().navigate(R.id.action_navigation_main_to_selectionFragment)
    }

    private fun onItemClickShowSecondCustom() {
        movieViewModel.chooseSelection(Selections.SecondCustom)
        findNavController().navigate(R.id.action_navigation_main_to_selectionFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
