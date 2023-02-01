package com.example.skillcinemaapp.ui.main


import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.skillcinemaapp.R
import com.example.skillcinemaapp.data.remote.person_dto.PersonFilm
import com.example.skillcinemaapp.databinding.FragmentPersonBinding
import com.example.skillcinemaapp.presentation.MovieViewModel
import com.example.skillcinemaapp.presentation.adapters.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest



@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
class PersonFragment : Fragment() {

    private var _binding: FragmentPersonBinding? = null
    private val binding get() = _binding!!

    private lateinit var backButton: AppCompatImageButton
    private lateinit var bestRecycler: RecyclerView
    private lateinit var personPhoto: AppCompatImageView
    private lateinit var personName: AppCompatTextView
    private lateinit var personDescription: AppCompatTextView
    private lateinit var extendActorsMovies: AppCompatImageButton
    private lateinit var allBestMovies: AppCompatTextView
    private lateinit var extendFilmography: AppCompatImageButton
    private lateinit var allPersonMovies: AppCompatTextView
    private lateinit var numberPersonMovies: AppCompatTextView
    private lateinit var photoUri: String


    private val bestMoviesAdapter = PersonMoviesAdapter(
        onPersonMovieItemClick = { personMovie -> onMovieClick(personMovie) },
        onShowAllPersonMoviesClick = { extendActorsMovies() }
    )

    private val movieViewModel: MovieViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backButton = binding.backButton
        personPhoto = binding.personPhoto
        personName = binding.personName
        personDescription = binding.personDescription
        extendActorsMovies = binding.extendBestMovies
        allBestMovies = binding.allBestMovies
        extendFilmography = binding.extendAllPersonMovies
        allPersonMovies = binding.allPersonMovies
        numberPersonMovies = binding.numberPersonMovies
        bestRecycler = binding.moviesOfPersonRecyclerView


        extendActorsMovies.setOnClickListener {
            extendActorsMovies()
        }

        allBestMovies.setOnClickListener {
            extendActorsMovies()
        }

        extendFilmography.setOnClickListener {
            extendFilmography()
        }

        allPersonMovies.setOnClickListener {
            extendFilmography()
        }

        personPhoto.setOnClickListener {
            showPhotoFullScreen()
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.person.collectLatest {
                if (it != null) {
                    Glide
                        .with(personPhoto.context)
                        .load(it.posterUrl)
                        .centerCrop()
                        .into(personPhoto)

                    photoUri = it.posterUrl

                    personName.text = it.nameRu ?: it.nameEn ?: ""
                    personDescription.text = it.profession ?: ""
                    numberPersonMovies.text = it.films?.size.toString()
                    movieViewModel.getBestPersonMovies()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.bestPersonMovies.collectLatest {
                bestRecycler.adapter = bestMoviesAdapter
                val bestMovies = it.take(11)
                bestMoviesAdapter.submitList(bestMovies)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.updateBestPersonMoviesPosters()
        }



        backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun onMovieClick(personMovie: PersonFilm) {
        movieViewModel.onAddMovieToDataBase(personMovie.filmId)
        movieViewModel.movieSelected(personMovie.filmId)
        movieViewModel.getImagesList(personMovie.filmId)
        movieViewModel.getStaffInfo(personMovie.filmId)
        movieViewModel.getActorsInfo(personMovie.filmId)
        movieViewModel.getSimilarMovies(personMovie.filmId)
        movieViewModel.getMovieInfo(personMovie.filmId)
        movieViewModel.getSeriesInfo(personMovie.filmId)
        movieViewModel.onInterestingButtonClick(personMovie.filmId)
        movieViewModel.getMovieFromDataBaseById(personMovie.filmId)
        findNavController().navigate(R.id.action_personFragment_to_movieDetailsFragment)
    }

    private fun extendFilmography() {
        findNavController().navigate(R.id.action_personFragment_to_filmographyFragment)
    }

    private fun extendActorsMovies() {
        findNavController().navigate(R.id.action_personFragment_to_bestPersonMoviesFragment)
    }

    private fun showPhotoFullScreen() {
        findNavController().navigate(R.id.action_personFragment_to_photoPopupFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
