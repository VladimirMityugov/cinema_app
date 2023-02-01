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
import androidx.recyclerview.widget.RecyclerView
import com.example.skillcinemaapp.R
import com.example.skillcinemaapp.data.remote.custom_selection_dto.ItemCustom
import com.example.skillcinemaapp.data.remote.popular_dto.Film
import com.example.skillcinemaapp.data.remote.premiers_dto.Item
import com.example.skillcinemaapp.databinding.FragmentSelectionBinding
import com.example.skillcinemaapp.presentation.MovieViewModel
import com.example.skillcinemaapp.presentation.utility.Selections
import com.example.skillcinemaapp.presentation.adapters.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import java.util.*


@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
class SelectionFragment : Fragment() {

    private var _binding: FragmentSelectionBinding? = null
    private val binding get() = _binding!!

    private lateinit var backButton: AppCompatImageButton

    private lateinit var selectionRecycler: RecyclerView

    private lateinit var selectionTitle: AppCompatTextView

    private val premiersAdapter = PremiersAdapterIndividual(
        onPremiersItemClick = { Item -> onItemClickPremiersSelection(Item) }
    )

    private val popularAdapter = PopularAdapterIndividual(
        onPopularItemClick = { Film -> onItemClickPopularSelection(Film) }
    )

    private val topAdapter = TopAdapterIndividual(
        onTopItemClick = { Film -> onItemClickTopSelection(Film) }
    )

    private val firstCustomAdapter = FirstCustomAdapterIndividual(
        onFirstCustomItemClick = { ItemCustom -> onItemClickFirstCustomSelection(ItemCustom) }
    )

    private val secondCustomAdapter = SecondCustomAdapterIndividual(
        onSecondCustomItemClick = { ItemCustom -> onItemClickSecondCustomSelection(ItemCustom) }
    )

    private val seriesAdapter = SeriesAdapterIndividual(
        onSeriesItemClick = { ItemCustom -> onItemClickSeriesSelection(ItemCustom) }
    )

    private val movieViewModel: MovieViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectionBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backButton = binding.backButton

        selectionRecycler = binding.selectionRecyclerView

        selectionTitle = binding.selectionTitle


        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.getAllWatched().collectLatest { list ->
                movieViewModel.getWatchedMovies(list)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.selectionChosen.collectLatest {
                when (it) {
                    Selections.Series -> {
                        selectionRecycler.adapter =
                            seriesAdapter.withLoadStateFooter(StateAdapter())
                        selectionTitle.text = SERIES
                        movieViewModel.getSeries().collectLatest {
                            seriesAdapter.submitData(it)
                        }
                    }
                    Selections.FirstCustom -> {
                        selectionRecycler.adapter =
                            firstCustomAdapter.withLoadStateFooter(StateAdapter())
                        val firstCountry =
                            movieViewModel.countries.value[movieViewModel.countryValueFirst.value!!-1].country
                        val firstGenre =
                            movieViewModel.genres.value[movieViewModel.genreValueFirst.value!!-1].genre?.replaceFirstChar { Char ->
                                if (Char.isLowerCase()) Char.titlecase(
                                    Locale.ROOT
                                ) else it.toString()
                            }
                        selectionTitle.text = buildString {
                            append(firstGenre)
                            append(" ")
                            append(firstCountry)
                        }
                        movieViewModel.getFirstCustomPagedSelection().collectLatest {
                                                       firstCustomAdapter.submitData(it)
                        }
                    }
                    Selections.Popular -> {
                        selectionRecycler.adapter =
                            popularAdapter.withLoadStateFooter(StateAdapter())
                        selectionTitle.text = POPULAR
                        movieViewModel.getPopular().collectLatest {
                            popularAdapter.submitData(it)
                        }
                    }
                    Selections.Premiers -> {
                        selectionRecycler.adapter = premiersAdapter
                        selectionTitle.text = PREMIERS
                        movieViewModel.premiers.collectLatest {
                            premiersAdapter.submitList(it)
                        }
                    }
                    Selections.SecondCustom -> {
                        selectionRecycler.adapter =
                            secondCustomAdapter.withLoadStateFooter(StateAdapter())
                        val secondCountry =
                            movieViewModel.countries.value[movieViewModel.countryValueSecond.value!!-1].country
                        val secondGenre =
                            movieViewModel.genres.value[movieViewModel.genreValueSecond.value!!-1].genre?.replaceFirstChar { Char ->
                            if (Char.isLowerCase()) Char.titlecase(
                                Locale.ROOT
                            ) else it.toString()
                        }
                        selectionTitle.text = buildString {
                            append(secondGenre)
                            append(" ")
                            append(secondCountry)
                        }
                        movieViewModel.getSecondCustomPagedSelection().collectLatest {
                                                      secondCustomAdapter.submitData(it)
                        }
                    }
                    Selections.Top -> {
                        selectionRecycler.adapter = topAdapter.withLoadStateFooter(StateAdapter())
                        selectionTitle.text = TOP
                        movieViewModel.getTop().collectLatest {
                            topAdapter.submitData(it)
                        }
                    }
                }
            }
        }

        backButton.setOnClickListener {
            findNavController().navigate(R.id.action_selectionFragment_to_navigation_main)
        }
    }

    private fun onItemClickFirstCustomSelection(item: ItemCustom) {
        movieViewModel.onAddMovieToDataBase(item.kinopoiskId)
        movieViewModel.movieSelected(item.kinopoiskId)
        movieViewModel.getImagesList(item.kinopoiskId)
        movieViewModel.getStaffInfo(item.kinopoiskId)
        movieViewModel.getActorsInfo(item.kinopoiskId)
        movieViewModel.getSimilarMovies(item.kinopoiskId)
        movieViewModel.getMovieInfo(item.kinopoiskId)
        movieViewModel.getSeriesInfo(item.kinopoiskId)
        movieViewModel.chooseSelection(Selections.FirstCustom)
        movieViewModel.onInterestingButtonClick(item.kinopoiskId)
        findNavController().navigate(R.id.action_selectionFragment_to_movieDetailsFragment)
    }

    private fun onItemClickSecondCustomSelection(item: ItemCustom) {
        movieViewModel.onAddMovieToDataBase(item.kinopoiskId)
        movieViewModel.movieSelected(item.kinopoiskId)
        movieViewModel.getImagesList(item.kinopoiskId)
        movieViewModel.getStaffInfo(item.kinopoiskId)
        movieViewModel.getActorsInfo(item.kinopoiskId)
        movieViewModel.getSimilarMovies(item.kinopoiskId)
        movieViewModel.getMovieInfo(item.kinopoiskId)
        movieViewModel.getSeriesInfo(item.kinopoiskId)
        movieViewModel.chooseSelection(Selections.SecondCustom)
        movieViewModel.onInterestingButtonClick(item.kinopoiskId)
        findNavController().navigate(R.id.action_selectionFragment_to_movieDetailsFragment)
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
        findNavController().navigate(R.id.action_selectionFragment_to_movieDetailsFragment)
    }

    private fun onItemClickTopSelection(item: Film) {
        movieViewModel.onAddMovieToDataBase(item.filmId)
        movieViewModel.movieSelected(item.filmId)
        movieViewModel.getImagesList(item.filmId)
        movieViewModel.getStaffInfo(item.filmId)
        movieViewModel.getActorsInfo(item.filmId)
        movieViewModel.getSimilarMovies(item.filmId)
        movieViewModel.getMovieInfo(item.filmId)
        movieViewModel.getSeriesInfo(item.filmId)
        movieViewModel.chooseSelection(Selections.Top)
        movieViewModel.onInterestingButtonClick(item.filmId)
        findNavController().navigate(R.id.action_selectionFragment_to_movieDetailsFragment)
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
        findNavController().navigate(R.id.action_selectionFragment_to_movieDetailsFragment)
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
        findNavController().navigate(R.id.action_selectionFragment_to_movieDetailsFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val SERIES = "Сериалы"
        private const val PREMIERS = "Премьеры"
        private const val POPULAR = "Популярное"
        private const val TOP = "Топ-250"
    }
}
