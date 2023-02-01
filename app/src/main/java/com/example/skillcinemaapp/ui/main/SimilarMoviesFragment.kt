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
import com.example.skillcinemaapp.data.remote.similar_movies.SimilarMovie
import com.example.skillcinemaapp.databinding.FragmentSimilarMoviesBinding
import com.example.skillcinemaapp.presentation.MovieViewModel
import com.example.skillcinemaapp.presentation.adapters.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest


@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
class SimilarMoviesFragment : Fragment() {

    private var _binding: FragmentSimilarMoviesBinding? = null
    private val binding get() = _binding!!

    private lateinit var backButton: AppCompatImageButton
    private lateinit var similarRecycler: RecyclerView
    private lateinit var similarMoviesTitle: AppCompatTextView

    private val similarAdapter = SimilarAdapterIndividual(
        onSimilarItemClick = { movie -> onSimilarClick(movie) }
    )

    private val movieViewModel: MovieViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSimilarMoviesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backButton = binding.backButton
        similarRecycler = binding.similarRecyclerView
        similarMoviesTitle = binding.similarMoviesTitle

        similarRecycler.adapter = similarAdapter

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.movieSelectedName.collectLatest {
                similarMoviesTitle.text = buildString {
                    append("Фильмы, похожие на ")
                    append(it)
                }
            }
        }


        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.similar.collectLatest {
                similarAdapter.submitList(it)
            }
        }

        backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }


    private fun onSimilarClick(movie: SimilarMovie) {
        movieViewModel.onAddMovieToDataBase(movie.filmId)
        movieViewModel.movieSelected(movie.filmId)
        movieViewModel.getImagesList(movie.filmId)
        movieViewModel.getStaffInfo(movie.filmId)
        movieViewModel.getActorsInfo(movie.filmId)
        movieViewModel.getSimilarMovies(movie.filmId)
        movieViewModel.getMovieInfo(movie.filmId)
        movieViewModel.getSeriesInfo(movie.filmId)
        movieViewModel.onInterestingButtonClick(movie.filmId)
        findNavController().navigate(R.id.action_similarMoviesFragment_to_movieDetailsFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
