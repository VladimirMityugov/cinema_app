package com.example.skillcinemaapp.ui.search

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.example.skillcinemaapp.R
import com.example.skillcinemaapp.data.remote.custom_selection_dto.ItemCustom
import com.example.skillcinemaapp.databinding.FragmentSearchBinding
import com.example.skillcinemaapp.presentation.utility.ConnectivityStatus
import com.example.skillcinemaapp.presentation.MovieViewModel
import com.example.skillcinemaapp.presentation.adapters.SearchResultAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var searchView: SearchView
    private lateinit var searchRecyclerView: RecyclerView
    private lateinit var settingsButton: AppCompatImageView
    private lateinit var errorMessage: AppCompatTextView

    private val searchAdapter = SearchResultAdapter(
        onSearchMovieItemClick = { itemCustom -> onSearchMovieItemClick(itemCustom) }
    )

    private val movieViewModel: MovieViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchView = binding.searchView
        searchRecyclerView = binding.searchRecyclerView
        searchRecyclerView.adapter = searchAdapter
        settingsButton = binding.filterButton
        errorMessage = binding.errorMessage

        settingsButton.setOnClickListener {
            onSettingsButtonClick()
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.getAllWatched().collectLatest { list ->
                movieViewModel.getWatchedMovies(list)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.searchQuery.collectLatest { query ->
                movieViewModel.getSearchResult(query).collectLatest {
                    searchAdapter.submitData(it)

                }
            }
        }

        searchAdapter.addLoadStateListener { loadStates ->
            if(loadStates.refresh != LoadState.Loading){
                if (searchAdapter.snapshot().items.isEmpty()) {
                    searchRecyclerView.isVisible = false
                    errorMessage.isVisible = true
                }
                else{
                    searchRecyclerView.isVisible = true
                    errorMessage.isVisible = false
                }
            }
        }

        searchView.clearFocus()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query == null || query.isEmpty()) {
                    movieViewModel.resetSearchQuery()
                } else {
                    movieViewModel.onSearchQueryInput(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText == null || newText.isEmpty()) {
                    movieViewModel.resetSearchQuery()
                } else {
                    movieViewModel.onSearchQueryInput(newText)
                }
                return true
            }
        })
    }


    private fun onSettingsButtonClick() {
        findNavController().navigate(R.id.action_navigation_search_to_searchSettingsFragment)
    }


    private fun onSearchMovieItemClick(itemCustom: ItemCustom) {
        if (itemCustom.watchedStatus == false || itemCustom.watchedStatus == null) {
            movieViewModel.onAddMovieToDataBase(itemCustom.kinopoiskId)
            movieViewModel.getMovieInfo(itemCustom.kinopoiskId)
        }
        movieViewModel.getMovieFromDataBaseById(itemCustom.kinopoiskId)
        movieViewModel.movieSelected(itemCustom.kinopoiskId)
        movieViewModel.getImagesList(itemCustom.kinopoiskId)
        movieViewModel.getStaffInfo(itemCustom.kinopoiskId)
        movieViewModel.getActorsInfo(itemCustom.kinopoiskId)
        movieViewModel.getSimilarMovies(itemCustom.kinopoiskId)
        movieViewModel.getSeriesInfo(itemCustom.kinopoiskId)
        movieViewModel.onInterestingButtonClick(itemCustom.kinopoiskId)
        findNavController().navigate(R.id.action_navigation_search_to_movieDetailsFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}