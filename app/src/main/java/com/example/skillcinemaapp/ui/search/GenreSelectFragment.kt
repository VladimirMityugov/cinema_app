package com.example.skillcinemaapp.ui.search

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.skillcinemaapp.R
import com.example.skillcinemaapp.data.remote.filters_dto.GenreFilters
import com.example.skillcinemaapp.databinding.FragmentGenreSelectBinding
import com.example.skillcinemaapp.presentation.MovieViewModel
import com.example.skillcinemaapp.presentation.adapters.SearchGenreSelectionAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
class GenreSelectFragment : Fragment() {

    private var _binding: FragmentGenreSelectBinding? = null
    private val binding get() = _binding!!

    private lateinit var backButton: AppCompatImageButton
    private lateinit var searchView: SearchView
    private lateinit var genreRecycler: RecyclerView
    private var listToDisplay = mutableListOf<GenreFilters>()

    private val genreAdapter = SearchGenreSelectionAdapter(
        onItemClick = { item -> onItemClick(item) }
    )


    private val movieViewModel: MovieViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGenreSelectBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backButton = binding.backButton
        searchView = binding.searchView
        genreRecycler = binding.genreRecycler
        genreRecycler.adapter = genreAdapter

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.genres.collectLatest {
                if(it.isNotEmpty()){
                    val list = mutableListOf<GenreFilters>()
                    it.forEach { if(it.genre!!.isNotEmpty()) list.add(it) }
                    genreAdapter.submitList(list)
                }
            }
        }

        searchView.clearFocus()

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.genres.collectLatest { genres ->

                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        if (query == null || query.isEmpty()) {
                            if(genres.isNotEmpty()){
                                val list = mutableListOf<GenreFilters>()
                                genres.forEach { if(it.genre!!.isNotEmpty()) list.add(it) }
                                genreAdapter.submitList(list)
                            }
                        }else{
                            listToDisplay.clear()
                            genres.forEach { if(it.genre!!.contains(query,true))listToDisplay.add(it) }
                            if(listToDisplay.isNotEmpty()){
                                genreAdapter.submitList(listToDisplay.toMutableList())
                            }
                        }
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        if (newText == null || newText.isEmpty()) {
                            if(genres.isNotEmpty()){
                                val list = mutableListOf<GenreFilters>()
                                genres.forEach { if(it.genre!!.isNotEmpty()) list.add(it) }
                                genreAdapter.submitList(list)
                            }
                        }else{
                            listToDisplay.clear()
                            val search = newText.lowercase()
                            genres.forEach { if(it.genre!!.contains(search,true))listToDisplay.add(it) }
                            if(listToDisplay.isNotEmpty()){
                                genreAdapter.submitList(listToDisplay.toMutableList())
                            }
                        }
                        return true
                    }
                })
            }

        }

        backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun onItemClick(genreFilters: GenreFilters){
        movieViewModel.setGenreForSearch(genreFilters.id)
        findNavController().navigate(R.id.action_genreSelectFragment_to_searchSettingsFragment)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}