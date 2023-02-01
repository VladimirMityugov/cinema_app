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
import com.example.skillcinemaapp.data.remote.person_dto.PersonFilm
import com.example.skillcinemaapp.databinding.FragmentBestPersonMoviesBinding
import com.example.skillcinemaapp.presentation.MovieViewModel
import com.example.skillcinemaapp.presentation.adapters.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

private const val TAG = "BEST_MOVIES_FRAG"

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
class BestPersonMoviesFragment : Fragment() {

    private var _binding: FragmentBestPersonMoviesBinding? = null
    private val binding get() = _binding!!

    private lateinit var backButton: AppCompatImageButton
    private lateinit var bestMoviesRecycler: RecyclerView
    private lateinit var bestMoviesTitle: AppCompatTextView

    private val bestMoviesAdapter = BestMoviesAdapter(
        onBestMovieItemClick = { personFilm -> onItemClickBestMovies(personFilm) }
    )


    private val movieViewModel: MovieViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBestPersonMoviesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backButton = binding.backButton
        bestMoviesTitle = binding.bestMoviesTitle
        bestMoviesRecycler = binding.bestMoviesRecyclerView

        bestMoviesRecycler.adapter = bestMoviesAdapter

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.person.collectLatest {
                bestMoviesTitle.text = buildString {
                    if (it != null) {
                        append(it.nameRu ?: it.nameEn ?: "")
                        append(".\n")
                        append("Лучшие фильмы")
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.bestPersonMovies.collectLatest {
                bestMoviesAdapter.submitList(it)
            }
        }

        backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun onItemClickBestMovies(personFilm: PersonFilm) {
        movieViewModel.onAddMovieToDataBase(personFilm.filmId)
        movieViewModel.movieSelected(personFilm.filmId)
        movieViewModel.getImagesList(personFilm.filmId)
        movieViewModel.getStaffInfo(personFilm.filmId)
        movieViewModel.getActorsInfo(personFilm.filmId)
        movieViewModel.getSimilarMovies(personFilm.filmId)
        movieViewModel.getMovieInfo(personFilm.filmId)
        movieViewModel.getSeriesInfo(personFilm.filmId)
        movieViewModel.onInterestingButtonClick(personFilm.filmId)
        findNavController().navigate(R.id.action_bestPersonMoviesFragment_to_movieDetailsFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
