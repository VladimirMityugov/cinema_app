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
import com.example.skillcinemaapp.data.remote.filters_dto.CountryFilters
import com.example.skillcinemaapp.databinding.FragmentCountrySelectBinding
import com.example.skillcinemaapp.presentation.MovieViewModel
import com.example.skillcinemaapp.presentation.adapters.SearchCountrySelectionAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
class CountrySelectFragment : Fragment() {

    private var _binding: FragmentCountrySelectBinding? = null
    private val binding get() = _binding!!

    private lateinit var backButton: AppCompatImageButton
    private lateinit var searchView: SearchView
    private lateinit var countryRecycler: RecyclerView
    private var listToDisplay = mutableListOf<CountryFilters>()

    private val countryAdapter = SearchCountrySelectionAdapter(
        onItemClick = {item -> onItemClick(item)}
    )


    private val movieViewModel: MovieViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCountrySelectBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backButton = binding.backButton
        searchView = binding.searchView
        countryRecycler = binding.countryRecycler
        countryRecycler.adapter = countryAdapter



        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.countries.collectLatest {

                if(it.isNotEmpty()){
                    val list = mutableListOf<CountryFilters>()
                    it.forEach { if(it.country!!.isNotEmpty()) list.add(it) }
                    countryAdapter.submitList(list)
                }
            }
        }

        searchView.clearFocus()


        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.countries.collectLatest { countries ->
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        if (query == null || query.isEmpty()) {
                            countryAdapter.submitList(countries.toMutableList())
                        }else{
                            listToDisplay.clear()
                            countries.forEach { if(it.country!!.contains(query,true))listToDisplay.add(it) }
                            if(listToDisplay.isNotEmpty()){
                                countryAdapter.submitList(listToDisplay.toMutableList())
                            }
                        }
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        if (newText == null || newText.isEmpty()) {
                            countryAdapter.submitList(countries.toMutableList())
                        }else{
                            listToDisplay.clear()
                            val search = newText.lowercase()
                            countries.forEach { if(it.country!!.contains(search,true))listToDisplay.add(it) }
                            if(listToDisplay.isNotEmpty()){
                                countryAdapter.submitList(listToDisplay.toMutableList())
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

    private fun onItemClick(countryFilters: CountryFilters){
        movieViewModel.setCountryForSearch(countryFilters.id)
        findNavController().navigate(R.id.action_countrySelectFragment_to_searchSettingsFragment)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}