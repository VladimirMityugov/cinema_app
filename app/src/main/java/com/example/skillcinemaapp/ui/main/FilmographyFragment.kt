package com.example.skillcinemaapp.ui.main


import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.skillcinemaapp.R
import com.example.skillcinemaapp.data.remote.person_dto.PersonFilm
import com.example.skillcinemaapp.databinding.FragmentFilmographyBinding
import com.example.skillcinemaapp.presentation.MovieViewModel
import com.example.skillcinemaapp.presentation.adapters.*
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import java.util.*


@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
class FilmographyFragment : Fragment() {

    private var _binding: FragmentFilmographyBinding? = null
    private val binding get() = _binding!!

    private lateinit var backButton: AppCompatImageButton
    private lateinit var personName: AppCompatTextView
    private lateinit var filmographyRecycler: RecyclerView
    private lateinit var chipGroup: ChipGroup

    private val filmographyAdapter = PersonFilmographyAdapter(
        onFilmographyMovieItemClick = { personFilm -> onMovieClick(personFilm) }
    )

    private val movieViewModel: MovieViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilmographyBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backButton = binding.backButton
        personName = binding.personName
        filmographyRecycler = binding.filmographyRecyclerView
        chipGroup = binding.chipGroup

        filmographyRecycler.adapter = filmographyAdapter


        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.person.collectLatest { personInfo ->

                personName.text = personInfo?.nameRu ?: personInfo?.nameEn ?: ""

                filmographyAdapter.submitList(personInfo?.films)

                if (personInfo != null) {
                    val professionsList = mutableListOf<String>()
                    personInfo.films?.forEach { professionsList.add(it.professionKey) }
                    val professionsListFiltered = professionsList.distinct()
                    val professionsListSize = professionsListFiltered.size
                    for (i in 1..professionsListSize) {
                        val chip = Chip(requireContext())
                        val contentDescription = professionsListFiltered.elementAt(i - 1).toString()
                            .lowercase(Locale.ROOT)
                            .replaceFirstChar { it.uppercaseChar() }
                        chip.contentDescription = contentDescription
                        chip.text = when (contentDescription) {
                            "Actor" -> if (personInfo.sex == "MALE") {
                                ACTOR
                            } else ACTRESS
                            "Producer" -> PRODUCER
                            "Writer" -> WRITER
                            "Director" -> DIRECTOR
                            "Himself" -> HIMSELF
                            "Herself" -> HERSELF
                            "Hrono_titr_male" -> HRONO_TITR_MALE
                            "Hrono_titr_female" -> HRONO_TITR_FEMALE
                            "Operator" -> OPERATOR
                            "Editor" -> EDITOR
                            "Composer" -> COMPOSER
                            "Translator" -> TRANSLATOR
                            "Design" -> DESIGN
                            "Voice_director" -> VOICE_DIRECTOR
                            else -> UNKNOWN
                        }
                        chip.id = i
                        chip.isClickable = true
                        chip.isCheckable = true
                        chipGroup.addView(chip)
                    }
                }

                chipGroup.children.forEach { chip ->
                    (chip as Chip).setOnCheckedChangeListener { buttonView, isChecked ->
                        if (isChecked) {
                            movieViewModel.getFilmographyFilter(buttonView.contentDescription as String)
                            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                                movieViewModel.moviesByProfession.collectLatest { personMovies ->
                                    val moviesListFiltered = mutableListOf<PersonFilm>()
                                    personMovies.forEach { movie ->
                                        moviesListFiltered.add(movie)
                                    }
                                    filmographyAdapter.submitList(moviesListFiltered)
                                }
                            }
                        } else {
                            filmographyAdapter.submitList(personInfo?.films)
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.updateFilmographyMoviesPosters()
        }


        backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun onMovieClick(personFilm: PersonFilm) {
        movieViewModel.onAddMovieToDataBase(personFilm.filmId)
        movieViewModel.movieSelected(personFilm.filmId)
        movieViewModel.getImagesList(personFilm.filmId)
        movieViewModel.getStaffInfo(personFilm.filmId)
        movieViewModel.getActorsInfo(personFilm.filmId)
        movieViewModel.getSimilarMovies(personFilm.filmId)
        movieViewModel.getMovieInfo(personFilm.filmId)
        movieViewModel.getSeriesInfo(personFilm.filmId)
        movieViewModel.onInterestingButtonClick(personFilm.filmId)
        movieViewModel.getMovieFromDataBaseById(personFilm.filmId)
        findNavController().navigate(R.id.action_filmographyFragment_to_movieDetailsFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ACTOR = "Актер"
        private const val ACTRESS = "Актриса"
        private const val PRODUCER = "Продюсер"
        private const val WRITER = "Сценарист"
        private const val DIRECTOR = "Режиссер"
        private const val HIMSELF = "Актер: Играет самого себя"
        private const val HERSELF = "Актриса: Играет саму себя"
        private const val HRONO_TITR_MALE = "Актер дубляжа"
        private const val HRONO_TITR_FEMALE = "Актриса дубляжа"
        private const val OPERATOR = "Оператор"
        private const val EDITOR = "Редактор"
        private const val COMPOSER = "Композитор"
        private const val TRANSLATOR = "Переводчик"
        private const val DESIGN = "Дизайнер"
        private const val VOICE_DIRECTOR = "Режиссер озвучания"
        private const val UNKNOWN = "Неизвестно"
    }
}
