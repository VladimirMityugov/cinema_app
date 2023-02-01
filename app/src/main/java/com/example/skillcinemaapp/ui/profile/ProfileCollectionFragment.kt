package com.example.skillcinemaapp.ui.profile

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.skillcinemaapp.R
import com.example.skillcinemaapp.data.local.entities.Movie
import com.example.skillcinemaapp.databinding.FragmentCollectionProfileBinding
import com.example.skillcinemaapp.presentation.utility.Collections
import com.example.skillcinemaapp.presentation.MovieViewModel
import com.example.skillcinemaapp.presentation.adapters.InterestingAdapterIndividual
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest


private const val TAG = "PROFILE_COLLECTION_FRAG"
@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
class ProfileCollectionFragment : Fragment() {

    private var _binding: FragmentCollectionProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var backButton: AppCompatImageButton
    private lateinit var collectionTitle : AppCompatTextView

    private lateinit var collectionRecycler: RecyclerView

    private val collectionAdapter = InterestingAdapterIndividual(
        onInterestingItemClick = { movie -> onItemClick(movie) }
    )

    private val movieViewModel: MovieViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectionProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backButton = binding.backButton

        collectionRecycler = binding.collectionRecyclerView
        collectionRecycler.adapter = collectionAdapter

        collectionTitle = binding.collectionTitle

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.getAllToWatch().collectLatest { list ->
                movieViewModel.buildToWatchList(list)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.getAllFavorites().collectLatest { list ->
                movieViewModel.buildFavoritesList(list)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.getAllMoviesFromCustomCollection().collectLatest {list ->
                movieViewModel.buildCustomCollectionList(list)
            }
        }


        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.collectionChosen.collectLatest {
                when(it){
                    Collections.Favorites-> {
                        collectionTitle.text = FAVORITES
                      movieViewModel.favoritesList.collectLatest {
                          if (it.isNotEmpty()) {
                              collectionRecycler.isVisible = true
                              Log.d(TAG, "There are ${it.size} movies in the list")
                              collectionAdapter.submitList(it)
                          } else collectionRecycler.isVisible  = false
                      }
                    }
                    Collections.Custom -> {
                        collectionTitle.text = movieViewModel.customCollectionChosen.value?.collectionName
                        movieViewModel.customCollectionList.collectLatest {
                            if (it.isNotEmpty()) {
                                collectionRecycler.isVisible = true
                                Log.d(TAG, "There are ${it.size} movies in the list")
                                collectionAdapter.submitList(it)
                            } else collectionRecycler.isVisible  = false
                        }
                    }
                    Collections.ToWatch -> {
                        collectionTitle.text = TO_WATCH
                        movieViewModel.toWatchList.collectLatest {
                            if (it.isNotEmpty()) {
                                collectionRecycler.isVisible = true
                                Log.d(TAG, "There are ${it.size} movies in the list")
                                collectionAdapter.submitList(it)
                            } else collectionRecycler.isVisible  = false
                        }
                    }
                }
            }
        }

        backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun onItemClick(movie: Movie) {
        Log.d(TAG, "Movie with id ${movie.movieId} is clicked")
        movieViewModel.movieSelected(movie.movieId)
        movieViewModel.getImagesList(movie.movieId)
        movieViewModel.getStaffInfo(movie.movieId)
        movieViewModel.getActorsInfo(movie.movieId)
        movieViewModel.getSimilarMovies(movie.movieId)
        movieViewModel.getSeriesInfo(movie.movieId)
        movieViewModel.getMovieFromDataBaseById(movie.movieId)
        findNavController().navigate(R.id.action_profileCollectionFragment_to_movieDetailsFragment)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    companion object {
        private const val FAVORITES = "Любимые фильмы"
        private const val TO_WATCH = "Хочу посмотреть"
    }
}