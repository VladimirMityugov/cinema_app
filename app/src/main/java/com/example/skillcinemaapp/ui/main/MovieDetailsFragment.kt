package com.example.skillcinemaapp.ui.main

import android.animation.LayoutTransition
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.TypefaceSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.skillcinemaapp.R
import com.example.skillcinemaapp.data.local.entities.Movie
import com.example.skillcinemaapp.data.remote.images_dto.Image
import com.example.skillcinemaapp.data.remote.similar_movies.SimilarMovie
import com.example.skillcinemaapp.data.remote.staff_dto.StaffDto
import com.example.skillcinemaapp.databinding.FragmentMovieDetailsBinding
import com.example.skillcinemaapp.presentation.MovieViewModel
import com.example.skillcinemaapp.presentation.utility.Selections
import com.example.skillcinemaapp.presentation.adapters.ActorAdapter
import com.example.skillcinemaapp.presentation.adapters.GalleryAdapterCommon
import com.example.skillcinemaapp.presentation.adapters.SimilarAdapterCommon
import com.example.skillcinemaapp.presentation.adapters.StaffAdapter
import com.example.skillcinemaapp.presentation.paging_sources.Persons
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


private const val TAG = "DETAILS_FRAG"

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
class MovieDetailsFragment : Fragment() {

    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var backButton: AppCompatImageButton
    private lateinit var movieLogo: AppCompatImageView
    private lateinit var movieTitle: AppCompatTextView
    private lateinit var moviePoster: AppCompatImageView
    private lateinit var movieAlternativeTitle: AppCompatTextView
    private lateinit var movieFirstField: AppCompatTextView
    private lateinit var movieSecondField: AppCompatTextView
    private lateinit var watchedIcon: AppCompatImageButton
    private lateinit var toWatchIcon: AppCompatImageButton
    private lateinit var favoritesIcon: AppCompatImageButton
    private lateinit var shareIcon: AppCompatImageButton
    private lateinit var otherIcon: AppCompatImageButton
    private lateinit var movieDescription: AppCompatTextView
    private lateinit var actorsNumber: AppCompatTextView
    private lateinit var extendActors: AppCompatImageButton
    private lateinit var actorsRecyclerView: RecyclerView
    private lateinit var staffNumber: AppCompatTextView
    private lateinit var extendStaff: AppCompatImageButton
    private lateinit var staffRecyclerView: RecyclerView
    private lateinit var moviesInGallery: AppCompatTextView
    private lateinit var extendGallery: AppCompatImageButton
    private lateinit var galleryRecyclerView: RecyclerView
    private lateinit var similarMoviesNumber: AppCompatTextView
    private lateinit var extendSimilarMovies: AppCompatImageButton
    private lateinit var similarMoviesRecyclerView: RecyclerView
    private lateinit var similarMoviesTitle: AppCompatTextView
    private lateinit var galleryTitle: AppCompatTextView
    private lateinit var seriesTitle: AppCompatTextView
    private lateinit var season: AppCompatTextView
    private lateinit var allSeries: AppCompatButton
    private lateinit var parentLayout: ConstraintLayout


    private val galleryAdapter = GalleryAdapterCommon(
        onImageClick = { image -> onImageClick(image) }
    )

    private val actorsAdapter = ActorAdapter(
        onActorClick = { actor -> onActorClick(actor) }
    )

    private val staffAdapter = StaffAdapter(
        onStaffClick = { person -> onStaffClick(person) }
    )

    private val similarAdapter = SimilarAdapterCommon(
        onSimilarItemClick = { movie -> onSimilarClick(movie) }
    )

    private val movieViewModel: MovieViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backButton = binding.backButton
        movieLogo = binding.detailsMovieLogo
        movieTitle = binding.detailsMovieTitle
        moviePoster = binding.movieDetailsBackground
        movieAlternativeTitle = binding.detailsMovieAlternativeTitle
        movieFirstField = binding.detailsMovieFirstField
        movieSecondField = binding.detailsMovieSecondField
        watchedIcon = binding.detailsWatchedIcon
        toWatchIcon = binding.detailsToWatch
        favoritesIcon = binding.detailsFavorites
        shareIcon = binding.detailsShareIcon
        otherIcon = binding.detailsOthersIcon
        movieDescription = binding.movieDescription
        actorsNumber = binding.actorsNumber
        extendActors = binding.extendActorsButton
        staffNumber = binding.staffNumber
        extendStaff = binding.extendStaffButton
        moviesInGallery = binding.moviesInGalleryNumber
        extendGallery = binding.extendGalleryButton
        similarMoviesNumber = binding.similarMoviesNumber
        extendSimilarMovies = binding.extendSimilarMovies
        actorsRecyclerView = binding.actorsRecyclerView
        similarMoviesRecyclerView = binding.similarMoviesRecyclerView
        staffRecyclerView = binding.staffRecyclerView
        galleryRecyclerView = binding.galleryRecyclerView
        similarMoviesTitle = binding.similarMoviesTitle
        galleryTitle = binding.galleryTitle
        seriesTitle = binding.seriesTitle
        allSeries = binding.allSeries
        season = binding.season

        galleryRecyclerView.adapter = galleryAdapter
        staffRecyclerView.adapter = staffAdapter
        actorsRecyclerView.adapter = actorsAdapter
        similarMoviesRecyclerView.adapter = similarAdapter

        parentLayout = binding.constraintLayoutMovieDetails

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.movieSelected.collectLatest { movieId ->
                movieViewModel.getMovieFromDataBaseById(movieId)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.getAllToWatch().collectLatest { list ->
                movieViewModel.movieSelected.collectLatest { movieId ->
                    movieViewModel.checkToWatch(movieId, list)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.getAllFavorites().collectLatest { list ->
                movieViewModel.movieSelected.collectLatest { movieId ->
                    movieViewModel.checkFavorites(movieId, list)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.getAllWatched().collectLatest { list ->
                movieViewModel.movieSelected.collectLatest { movieId ->
                    movieViewModel.checkWatched(movieId, list)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.addedToFavorites.collectLatest {
                favoritesIcon.isActivated = it
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.addedToWatch.collectLatest {
                toWatchIcon.isActivated = it
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.addedToWatched.collectLatest { isAdded ->
                watchedIcon.isActivated = isAdded
                movieViewModel.movieSelected.collectLatest { id ->
                    movieViewModel.selectionChosen.collectLatest { selection ->
                        when (selection) {
                            Selections.FirstCustom -> {
                                movieViewModel.firstCustom.collectLatest { movieList ->
                                    movieList.forEach {
                                        if (it.kinopoiskId == id) it.watchedStatus = isAdded
                                    }
                                }
                            }
                            Selections.Popular -> {
                                movieViewModel.popular.collectLatest { movieList ->
                                    movieList.forEach {
                                        if (it.filmId == id) it.watchedStatus = isAdded
                                    }
                                }
                            }
                            Selections.Premiers -> {
                                movieViewModel.premiers.collectLatest { movieList ->
                                    movieList.forEach {
                                        if (it.kinopoiskId == id) it.watched_status = isAdded
                                    }
                                }
                            }
                            Selections.SecondCustom -> {
                                movieViewModel.secondCustom.collectLatest { movieList ->
                                    movieList.forEach {
                                        if (it.kinopoiskId == id) it.watchedStatus = isAdded
                                    }
                                }
                            }
                            Selections.Series -> {
                                movieViewModel.series.collectLatest { movieList ->
                                    movieList.forEach {
                                        if (it.kinopoiskId == id) it.watchedStatus = isAdded
                                    }
                                }
                            }
                            Selections.Top -> {
                                movieViewModel.top.collectLatest { movieList ->
                                    movieList.forEach {
                                        if (it.filmId == id) it.watchedStatus = isAdded
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        favoritesIcon.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                movieViewModel.movieSelected.collectLatest { movieId ->
                    movieViewModel.onFavoritesButtonClick(movieId)
                }
            }
        }

        toWatchIcon.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                movieViewModel.movieSelected.collectLatest { movieId ->
                    movieViewModel.onToWatchButtonClick(movieId)
                }
            }
        }

        watchedIcon.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                movieViewModel.movieSelected.collectLatest { movieId ->
                    movieViewModel.onWatchedButtonClick(movieId)
                }
            }
        }

        shareIcon.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                movieViewModel.movieInfo.collectLatest {
                    val share = Intent(Intent.ACTION_SEND)
                    share.type = "text/plain"
                    if (it != null) {
                        share.putExtra(
                            Intent.EXTRA_TEXT,
                            "https://www.imdb.com/title/${it.imdbId}/"
                        )
                    }
                    startActivity(Intent.createChooser(share, "Share Link"))
                }
            }

        }

        otherIcon.setOnClickListener {
            onClickCollectionHandler()
        }

        extendGallery.setOnClickListener {
            onClickShowImages()
        }

        extendStaff.setOnClickListener {
            onClickShowStaff()
        }

        extendActors.setOnClickListener {
            onClickShowActors()
        }

        extendSimilarMovies.setOnClickListener {
            onClickShowSimilarMovies()
        }

        allSeries.setOnClickListener {
            onClickShowAllSeries()
        }


        backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.similar.collectLatest {
                Log.d(TAG, "Data is  $it")
                if (it.isNotEmpty()) {
                    similarMoviesRecyclerView.isVisible = true
                    similarMoviesTitle.isVisible = true
                    if (it.size > 20) {
                        extendSimilarMovies.isVisible = true
                        similarMoviesNumber.isVisible = true
                        similarMoviesNumber.text = it.size.toString()
                        similarAdapter.submitList(it.take(20))
                    } else similarAdapter.submitList(it)
                } else {
                    similarMoviesRecyclerView.isVisible = false
                    similarMoviesTitle.isVisible = false
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.movieInfo.collectLatest { movie ->
                movieViewModel.movieById.collectLatest { movieFromDB ->
                    Log.d(TAG, "Movie from DB is $movieFromDB")
                    if (movieFromDB != null) getMovieInfoFromDataBase(movieFromDB)
                    else {
                        if (movie != null) {
//                    Glide
//                        .with(moviePoster.context)
//                        .load(it.posterUrlPreview)
//                        .centerCrop()
//                        .into(moviePoster)

                            MOVIE_NAME =
                                movie.nameRu ?: movie.nameEn ?: movie.nameOriginal ?: ""

                            if (movie.logoUrl != null) {
                                movieLogo.isVisible = true
                                Glide
                                    .with(movieLogo.context)
                                    .load(movie.logoUrl)
                                    .into(movieLogo)
                                movieTitle.isVisible = false
                            } else {
                                movieLogo.isVisible = false
                                movieTitle.isVisible = true
                                movieTitle.text =
                                    movie.nameRu ?: movie.nameEn ?: movie.nameOriginal ?: ""
                            }

                            movieSecondField.text = buildString {
                                append(movie.countries?.firstOrNull()?.country ?: "")
                                if (movie.filmLength != null) {
                                    append(", ")
                                    append(timeConverter(movie.filmLength))
                                } else append("")
                                if (movie.ratingAgeLimits != null) {
                                    append(", ")
                                    append(movie.ratingAgeLimits.drop(3))
                                    append("+")
                                } else append("")
                            }

                            movieAlternativeTitle.text = buildString {
                                val rating = when {
                                    movie.ratingKinopoisk != null -> movie.ratingKinopoisk.toString()
                                    movie.ratingImdb != null -> movie.ratingImdb.toString()
                                    movie.ratingMpaa != null -> movie.ratingMpaa
                                    else -> ""
                                }
                                append(rating)
                                append(" ")
                                append(movie.nameOriginal ?: movie.nameEn ?: "")
                            }

                            val shortDescription: SpannableString
                            val description: SpannableString
                            val spannableString: String
                            val myTypeface = Typeface.create(
                                ResourcesCompat.getFont(
                                    requireContext(),
                                    R.font.graphik_regular
                                ),
                                Typeface.BOLD
                            )

                            if (movie.shortDescription != null && movie.description != null) {
                                shortDescription = SpannableString(movie.shortDescription)
                                description = SpannableString(movie.description)

                                shortDescription.setSpan(
                                    TypefaceSpan(myTypeface),
                                    0,
                                    shortDescription.length,
                                    Spannable.SPAN_INCLUSIVE_EXCLUSIVE
                                )
                                description.setSpan(
                                    ForegroundColorSpan(Color.RED),
                                    25,
                                    description.length,
                                    Spannable.SPAN_EXCLUSIVE_INCLUSIVE
                                )
                                spannableString =
                                    shortDescription.toString() + "\n\n" + description.toString()

                                if (spannableString.length >= 250) {
                                    SHORT_TEXT = spannableString.take(250) + "..."
                                    WHOLE_TEXT = spannableString
                                } else {
                                    WHOLE_TEXT = spannableString
                                    SHORT_TEXT = spannableString
                                }
                                movieDescription.text = SHORT_TEXT
                            } else if (movie.shortDescription != null && movie.description == null) {
                                shortDescription = SpannableString(movie.shortDescription)
                                shortDescription.setSpan(
                                    TypefaceSpan(myTypeface),
                                    0,
                                    shortDescription.length,
                                    Spannable.SPAN_INCLUSIVE_EXCLUSIVE
                                )
                                spannableString = shortDescription.toString()

                                if (spannableString.length >= 250) {
                                    SHORT_TEXT = spannableString.take(250) + "..."
                                    WHOLE_TEXT = spannableString
                                } else {
                                    WHOLE_TEXT = spannableString
                                    SHORT_TEXT = spannableString
                                }
                                movieDescription.text = SHORT_TEXT
                            } else if (movie.shortDescription == null && movie.description != null) {
                                description = SpannableString(movie.description)
                                description.setSpan(
                                    TypefaceSpan(myTypeface),
                                    0,
                                    description.length,
                                    Spannable.SPAN_EXCLUSIVE_INCLUSIVE
                                )
                                spannableString = description.toString()

                                if (spannableString.length >= 250) {
                                    SHORT_TEXT = spannableString.take(250) + "..."
                                    WHOLE_TEXT = spannableString
                                } else {
                                    WHOLE_TEXT = spannableString
                                    SHORT_TEXT = spannableString
                                }
                                movieDescription.text = SHORT_TEXT
                            } else movieDescription.text = ""

                            if (movie.serial != null && movie.serial == true) {
                                seriesTitle.isVisible = true
                                allSeries.isVisible = true
                                season.isVisible = true



                                movieFirstField.text = buildString {
                                    append(movie.year ?: movie.startYear ?: movie.endYear ?: "")
                                    append(", ")
                                    append(movie.genres?.firstOrNull()?.genre ?: "")
                                    val seasonNumbers = movieViewModel.seriesInfo.value?.total
                                    if (seasonNumbers != null) {
                                        append(", ")
                                        append(seasonNumbers)
                                        append(
                                            when (seasonNumbers) {
                                                0 -> ""
                                                1 -> " сезон"
                                                in 2..4 -> " сезона"
                                                else -> " сезонов"
                                            }
                                        )
                                    }
                                }

                                movieViewModel.seriesInfo.collectLatest {
                                    if (it != null) season.text = buildString {
                                        val seasonsNumber = it.total
                                        var episodeNumber = 0
                                        it.items.forEach { series ->
                                            episodeNumber += series.episodes.lastOrNull()?.episodeNumber!!
                                        }
                                        append(
                                            seasonsNumber
                                        )
                                        append(
                                            when (seasonsNumber) {
                                                0 -> ""
                                                1 -> " сезон, "
                                                in 2..4 -> " сезона, "
                                                else -> " сезонов, "
                                            }
                                        )
                                        append(
                                            episodeNumber
                                        )
                                        Log.d(TAG, "Episode numbers is $episodeNumber")
                                        append(
                                            if (episodeNumber > 0) {
                                                when (episodeNumber.toString().takeLast(1)) {
                                                    "0" -> " серий"
                                                    "1" -> " серия."
                                                    "2", "3", "4" -> " серии."
                                                    else -> " серий."
                                                }
                                            } else ""
                                        )
                                    }
                                }
                            } else {
                                seriesTitle.visibility = View.GONE
                                allSeries.visibility = View.GONE
                                season.visibility = View.GONE

                                movieFirstField.text = buildString {
                                    append(movie.year ?: movie.startYear ?: movie.endYear ?: "")
                                    append(", ")
                                    append(movie.genres?.firstOrNull()?.genre ?: "")
                                    append(", ")
                                    append(if (movie.genres?.size!! >= 2) movie.genres[1].genre else "")
                                }
                            }
                        }
                    }

                }
            }
        }

        var isCollapsed = true

        movieDescription.setOnClickListener {
            MAX_LINE_COLLAPSED = movieDescription.lineCount
            applyLayoutTransition()
            if (isCollapsed) {
                movieDescription.text = WHOLE_TEXT
                movieDescription.maxLines = Integer.MAX_VALUE
                isCollapsed = false
            } else {
                movieDescription.text = SHORT_TEXT
                movieDescription.maxLines = MAX_LINE_COLLAPSED
                isCollapsed = true

            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                movieViewModel.staff.collectLatest {
                    if (it.isNotEmpty()) {
                        Log.d(TAG, "There are ${it.size} staff")
                        staffRecyclerView.isVisible = true
                        if (it.size > 6) {
                            extendStaff.isVisible = true
                            staffNumber.isVisible = true
                            staffNumber.text = it.size.toString()
                            staffAdapter.submitList(it.take(6))
                        } else staffAdapter.submitList(it)
                    } else {
                        Log.d(TAG, "There are ${it.size} staff")
                        extendStaff.isVisible = false
                        staffNumber.isVisible = false
                        staffRecyclerView.isVisible = false
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                movieViewModel.actors.collectLatest {
                    if (it.isNotEmpty()) {
                        Log.d(TAG, "There are ${it.size} actors")
                        actorsRecyclerView.isVisible = true
                        if (it.size > 20) {
                            extendActors.isVisible = true
                            actorsNumber.isVisible = true
                            actorsNumber.text = it.size.toString()
                            actorsAdapter.submitList(it.take(20))
                        } else actorsAdapter.submitList(it)
                    } else {
                        Log.d(TAG, "There are ${it.size} actors")
                        extendActors.isVisible = false
                        actorsNumber.isVisible = false
                        actorsRecyclerView.isVisible = false
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                movieViewModel.images.collectLatest {
                    Log.d(TAG, "There are ${it?.total} images to show ")
                    if (it != null) {
                        galleryRecyclerView.isVisible = true
                        galleryTitle.isVisible = true
                        if (it.total > 20) {
                            extendGallery.isVisible = true
                            moviesInGallery.isVisible = true
                            moviesInGallery.text = it.total.toString()
                            galleryAdapter.submitList(it.items.take(20))
                        } else galleryAdapter.submitList(it.items)
                    } else {
                        galleryRecyclerView.isVisible = false
                        galleryTitle.isVisible = false
                    }
                }
            }
        }
    }

    private fun applyLayoutTransition() {
        val transition = LayoutTransition()
        transition.setDuration(700)
        transition.enableTransitionType(LayoutTransition.CHANGING)
        parentLayout.layoutTransition = transition
    }

    private fun onImageClick(image: Image) {
        movieViewModel.imageSelected(image)
        findNavController().navigate(R.id.action_movieDetailsFragment_to_collectionGalleryFragment)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun getMovieInfoFromDataBase(movie: Movie) {

        MOVIE_NAME = movie.movieName ?: movie.nameEn ?: ""

        if (movie.logoUrl != null) {
            movieLogo.isVisible = true
            Glide
                .with(movieLogo.context)
                .load(movie.logoUrl)
                .into(movieLogo)
            movieTitle.isVisible = false
        } else {
            movieLogo.isVisible = false
            movieTitle.isVisible = true
            movieTitle.text = movie.movieName ?: movie.nameEn ?: ""
        }

        movieSecondField.text = buildString {
            append(movie.country ?: "")
            if (movie.filmLength != null) {
                append(", ")
                append(timeConverter(movie.filmLength))
            } else append("")
            if (movie.ratingAgeLimits != null) {
                append(", ")
                append(movie.ratingAgeLimits.drop(3))
                append("+")
            } else append("")
        }

        movieAlternativeTitle.text = buildString {
            val rating = when {
                movie.rating != null -> movie.rating.toString()
                else -> ""
            }
            append(rating)
            append(" ")
            append(movie.nameEn ?: movie.movieName ?: "")
        }

        movieFirstField.text = buildString {
            append(movie.year ?: "")
            append(", ")
            append(movie.genre ?: "")
        }

        val shortDescription: SpannableString
        val description: SpannableString
        val spannableString: String
        val myTypeface = Typeface.create(
            ResourcesCompat.getFont(requireContext(), R.font.graphik_regular),
            Typeface.BOLD
        )

        if (movie.shortDescription != null && movie.description != null) {
            shortDescription = SpannableString(movie.shortDescription)
            description = SpannableString(movie.description)

            shortDescription.setSpan(
                TypefaceSpan(myTypeface),
                0,
                shortDescription.length,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE
            )
            description.setSpan(
                ForegroundColorSpan(Color.RED),
                25,
                description.length,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
            )
            spannableString =
                shortDescription.toString() + "\n\n" + description.toString()

            if (spannableString.length >= 250) {
                SHORT_TEXT = spannableString.take(250) + "..."
                WHOLE_TEXT = spannableString
            } else {
                WHOLE_TEXT = spannableString
                SHORT_TEXT = spannableString
            }
            movieDescription.text = SHORT_TEXT
        } else if (movie.shortDescription != null && movie.description == null) {
            shortDescription = SpannableString(movie.shortDescription)
            shortDescription.setSpan(
                TypefaceSpan(myTypeface),
                0,
                shortDescription.length,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE
            )
            spannableString = shortDescription.toString()

            if (spannableString.length >= 250) {
                SHORT_TEXT = spannableString.take(250) + "..."
                WHOLE_TEXT = spannableString
            } else {
                WHOLE_TEXT = spannableString
                SHORT_TEXT = spannableString
            }
            movieDescription.text = SHORT_TEXT
        } else if (movie.shortDescription == null && movie.description != null) {
            description = SpannableString(movie.description)
            description.setSpan(
                TypefaceSpan(myTypeface),
                0,
                description.length,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
            )
            spannableString = description.toString()

            if (spannableString.length >= 250) {
                SHORT_TEXT = spannableString.take(250) + "..."
                WHOLE_TEXT = spannableString
            } else {
                WHOLE_TEXT = spannableString
                SHORT_TEXT = spannableString
            }
            movieDescription.text = SHORT_TEXT
        } else movieDescription.text = ""

        if (movie.serial != null && movie.serial == true) {
            seriesTitle.isVisible = true
            allSeries.isVisible = true
            season.isVisible = true



            movieFirstField.text = buildString {
                append(movie.year ?: "")
                append(", ")
                append(movie.genre ?: "")
                val seasonNumbers = movieViewModel.seriesInfo.value?.total
                if (seasonNumbers != null) {
                    append(", ")
                    append(seasonNumbers)
                    append(
                        when (seasonNumbers) {
                            0 -> ""
                            1 -> " сезон"
                            in 2..4 -> " сезона"
                            else -> " сезонов"
                        }
                    )
                }
            }

            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                movieViewModel.seriesInfo.collectLatest {
                    if (it != null) {
                        season.text = buildString {
                            val seasonsNumber = it.total
                            var episodeNumber = 0
                            it.items.forEach { series ->
                                episodeNumber += series.episodes.lastOrNull()?.episodeNumber!!
                            }
                            append(
                                seasonsNumber
                            )
                            append(
                                when (seasonsNumber) {
                                    0 -> ""
                                    1 -> " сезон, "
                                    in 2..4 -> " сезона, "
                                    else -> " сезонов, "
                                }
                            )
                            append(
                                episodeNumber
                            )
                            Log.d(TAG, "Episode numbers is $episodeNumber")
                            append(
                                if (episodeNumber > 0) {
                                    when (episodeNumber.toString().takeLast(1)) {
                                        "0" -> " серий"
                                        "1" -> " серия."
                                        "2", "3", "4" -> " серии."
                                        else -> " серий."
                                    }
                                } else ""
                            )
                        }
                    } else {
                        seriesTitle.visibility = View.GONE
                        allSeries.visibility = View.GONE
                        season.visibility = View.GONE

                        movieFirstField.text = buildString {
                            append(movie.year ?:  "")
                            append(", ")
                            append(movie.genre ?: "")
                        }
                    }
                }

            }
        }
    }


    private fun onClickShowStaff() {
        movieViewModel.movieSelectedName(MOVIE_NAME)
        movieViewModel.choosePersonType(Persons.Staff)
        findNavController().navigate(R.id.action_movieDetailsFragment_to_staffFragment)
    }

    private fun onClickShowActors() {
        movieViewModel.movieSelectedName(MOVIE_NAME)
        movieViewModel.choosePersonType(Persons.Actors)
        findNavController().navigate(R.id.action_movieDetailsFragment_to_staffFragment)
    }

    private fun onClickShowAllSeries() {
        findNavController().navigate(R.id.action_movieDetailsFragment_to_seriesFragment)
    }

    private fun onClickShowImages() {
        findNavController().navigate(R.id.action_movieDetailsFragment_to_imagesFragment)
    }

    private fun onSimilarClick(movie: SimilarMovie) {
        Log.d(TAG, "Movie with id  ${movie.filmId} is clicked")
        movieViewModel.movieSelected(movie.filmId)
        movieViewModel.getImagesList(movie.filmId)
        movieViewModel.getStaffInfo(movie.filmId)
        movieViewModel.getActorsInfo(movie.filmId)
        movieViewModel.getSimilarMovies(movie.filmId)
        movieViewModel.getMovieInfo(movie.filmId)
        movieViewModel.onInterestingButtonClick(movie.filmId)
        movieViewModel.onAddMovieToDataBase(movie.filmId)
    }

    private fun onClickShowSimilarMovies() {
        movieViewModel.movieSelectedName(MOVIE_NAME)
        findNavController().navigate(R.id.action_movieDetailsFragment_to_similarMoviesFragment)
    }

    private fun onActorClick(actor: StaffDto) {
        Log.d(TAG, "Actor with id ${actor.staffId} is clicked")
        movieViewModel.getPersonInfo(actor.staffId)
        findNavController().navigate(R.id.action_movieDetailsFragment_to_personFragment)
    }

    private fun onStaffClick(person: StaffDto) {
        Log.d(TAG, "Person with id ${person.staffId} is clicked")
        movieViewModel.getPersonInfo(person.staffId)
        findNavController().navigate(R.id.action_movieDetailsFragment_to_personFragment)
    }

    private fun onClickCollectionHandler() {
        val popupWindow = CollectionHandlerFragment()
        popupWindow.setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogTheme)
        popupWindow.enterTransition = com.google.android.material.R.id.animateToStart
        popupWindow.exitTransition = com.google.android.material.R.id.animateToEnd
        popupWindow.show(requireActivity().supportFragmentManager, "POP_UP")
    }

    private fun timeConverter(time: Int): String {
        val hours = time / 60
        val minutes = time - hours * 60
        val convertedTime = buildString {
            if (hours > 0) append(hours)
            append(
                when (hours) {
                    0 -> ""
                    else -> " ч "
                }
            )
            append(minutes)
            append(" мин")
        }
        return convertedTime
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        private var MAX_LINE_COLLAPSED = 0
        private var SHORT_TEXT = ""
        private var WHOLE_TEXT = ""
        private var MOVIE_NAME = ""
    }

}