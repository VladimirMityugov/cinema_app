package com.example.skillcinemaapp.ui.profile

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.skillcinemaapp.R
import com.example.skillcinemaapp.data.local.entities.Movie
import com.example.skillcinemaapp.databinding.FragmentInterestingBinding
import com.example.skillcinemaapp.databinding.FragmentWatchedBinding
import com.example.skillcinemaapp.presentation.MovieViewModel
import com.example.skillcinemaapp.presentation.adapters.InterestingAdapterIndividual
import com.example.skillcinemaapp.presentation.adapters.WatchedAdapterIndividual
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest


private const val TAG = "INTERESTING_FRAG"

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
class InterestingFragment : Fragment() {

    private var _binding: FragmentInterestingBinding? = null
    private val binding get() = _binding!!

    private lateinit var backButton: AppCompatImageButton

    private lateinit var interestingRecycler: RecyclerView

    private val interestingAdapter = InterestingAdapterIndividual(
        onInterestingItemClick = { movie -> onItemClickInteresting(movie) }
    )


    private val movieViewModel: MovieViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInterestingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backButton = binding.backButton

        interestingRecycler = binding.interestingRecyclerView
        interestingRecycler.adapter = interestingAdapter

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.interestingList.collectLatest {
                if (it.isNotEmpty()) {
                    interestingRecycler.isVisible = true
                    interestingAdapter.submitList(it)
                } else interestingRecycler.isVisible = false
            }
        }

        backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun onItemClickInteresting(movie: Movie) {
        movieViewModel.movieSelected(movie.movieId)
        movieViewModel.getImagesList(movie.movieId)
        movieViewModel.getStaffInfo(movie.movieId)
        movieViewModel.getActorsInfo(movie.movieId)
        movieViewModel.getSimilarMovies(movie.movieId)
        movieViewModel.getSeriesInfo(movie.movieId)
        movieViewModel.getMovieFromDataBaseById(movie.movieId)
        findNavController().navigate(R.id.action_interestingFragment_to_movieDetailsFragment)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}