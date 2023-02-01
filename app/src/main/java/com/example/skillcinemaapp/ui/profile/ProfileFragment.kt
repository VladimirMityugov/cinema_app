package com.example.skillcinemaapp.ui.profile

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.*
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.skillcinemaapp.R
import com.example.skillcinemaapp.data.local.entities.CustomCollection
import com.example.skillcinemaapp.data.local.entities.Movie
import com.example.skillcinemaapp.databinding.FragmentProfileBinding
import com.example.skillcinemaapp.presentation.utility.Collections
import com.example.skillcinemaapp.presentation.MovieViewModel
import com.example.skillcinemaapp.presentation.adapters.CustomCollectionAdapter
import com.example.skillcinemaapp.presentation.adapters.InterestingMoviesAdapterCommon
import com.example.skillcinemaapp.presentation.adapters.WatchedAdapterCommon
import com.example.skillcinemaapp.ui.main.ErrorFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import java.util.*


private const val TAG = "PROFILE_FRAGMENT"

@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var loader: AppCompatImageView
    private lateinit var allWatchedNumber: AppCompatButton
    private lateinit var extendWatched: AppCompatImageButton
    private lateinit var watchedRecyclerView: RecyclerView
    private lateinit var createCollectionButton: AppCompatImageButton
    private lateinit var createCollection: AppCompatButton
    private lateinit var numberInFavoritesCollection: AppCompatTextView
    private lateinit var numberInToWatchCollection: AppCompatTextView
    private lateinit var allInterestingNumber: AppCompatButton
    private lateinit var extendInteresting: AppCompatImageButton
    private lateinit var interestingRecyclerView: RecyclerView
    private lateinit var favoritesCollection: FrameLayout
    private lateinit var toWatchCollection: FrameLayout
    private lateinit var collectionRecyclerView: RecyclerView

    private val watchedAdapter = WatchedAdapterCommon(
        onWatchedItemClick = { movie -> onWatchedItemClick(movie) },
        onClearHistoryClick = { onClearWatchedClick() }
    )

    private val interestingAdapter = InterestingMoviesAdapterCommon(
        onInterestingItemClick = { movie -> onInterestingItemClick(movie) },
        onClearInterestingClick = { onClearInterestingClick() }
    )

    private val collectionAdapter = CustomCollectionAdapter(
        onCollectionItemClick = { customCollection -> onCollectionItemClick(customCollection) },
        onDeleteCollectionClick = { collectionName -> onDeleteCollectionButtonClick(collectionName) }
    )


    private val movieViewModel: MovieViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loader = binding.loader
        allWatchedNumber = binding.allWatchedNumber
        extendWatched = binding.extendWatched
        watchedRecyclerView = binding.watchedRecyclerView
        createCollectionButton = binding.createCollectionButton
        createCollection = binding.createCustomCollectionText
        numberInFavoritesCollection = binding.numberInFavoritsCollection
        numberInToWatchCollection = binding.numberInToWatchCollection
        allInterestingNumber = binding.allInterestingNumber
        extendInteresting = binding.extendInteresting
        interestingRecyclerView = binding.interestingRecyclerView
        favoritesCollection = binding.favoritsCollection
        toWatchCollection = binding.toWatchCollection
        collectionRecyclerView = binding.containerLayoutForCustomCollections

        watchedRecyclerView.adapter = watchedAdapter
        interestingRecyclerView.adapter = interestingAdapter
        collectionRecyclerView.adapter = collectionAdapter


        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.getAllMoviesFromCustomCollection().collectLatest { list ->
                createCollection.setOnClickListener {
                    createCollection(list)
                }
                createCollectionButton.setOnClickListener {
                    createCollection(list)
                }
            }
        }


        favoritesCollection.setOnClickListener {
            movieViewModel.chooseCollection(Collections.Favorites)
            findNavController().navigate(R.id.action_navigation_profile_to_profileCollectionFragment)
        }

        toWatchCollection.setOnClickListener {
            movieViewModel.chooseCollection(Collections.ToWatch)
            findNavController().navigate(R.id.action_navigation_profile_to_profileCollectionFragment)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.getAllMoviesFromCustomCollection().collectLatest { list ->
                movieViewModel.getCustomCollectionNames(list)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.getAllMoviesFromCustomCollection().collectLatest { list->
                movieViewModel.getCustomCollections(list)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.getAllInteresting().collectLatest { list ->
                movieViewModel.buildInterestingList(list)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.getAllWatched().collectLatest { list ->
                movieViewModel.buildWatchedList(list)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.getAllWatched().collectLatest {
                allWatchedNumber.text = it.size.toString()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.getAllInteresting().collectLatest {
                allInterestingNumber.text = it.size.toString()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.customCollections.collectLatest {
                if (it.isNotEmpty()) {
                    collectionRecyclerView.isVisible = true
                    Log.d(TAG, "Custom collection is $it")
                    collectionAdapter.submitList(it)
                } else collectionRecyclerView.isVisible = false
            }
        }

        allWatchedNumber.setOnClickListener {
            extendWatched()
        }
        extendWatched.setOnClickListener {
            extendWatched()
        }

        allInterestingNumber.setOnClickListener {
            extendInteresting()
        }
        extendInteresting.setOnClickListener {
            extendInteresting()
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.getAllFavorites().collectLatest {
                if (it.isNotEmpty()) {
                    numberInFavoritesCollection.isVisible = true
                    numberInFavoritesCollection.text = it.size.toString()
                } else numberInFavoritesCollection.isVisible = false
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.getAllToWatch().collectLatest {
                if (it.isNotEmpty()) {
                    numberInToWatchCollection.isVisible = true
                    numberInToWatchCollection.text = it.size.toString()
                } else numberInToWatchCollection.isVisible = false
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.interestingList.collectLatest {
                if (it.isNotEmpty()) {
                    interestingRecyclerView.isVisible = true
                    Log.d(TAG, "There are ${it.size} movies in interesting list")
                    interestingAdapter.submitList(it.take(11))
                } else interestingRecyclerView.isVisible = false
            }
        }


        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.watchedList.collectLatest {
                if (it.isNotEmpty()) {
                    watchedRecyclerView.isVisible = true
                    Log.d(TAG, "There are ${it.size} movies in watched list")
                    watchedAdapter.submitList(it.take(11))
                } else watchedRecyclerView.isVisible = false
            }
        }
    }

    private fun onClearWatchedClick() {
        movieViewModel.onCleanWatchedClick()
        watchedRecyclerView.isVisible = false
    }

    private fun onWatchedItemClick(movie: Movie) {
        Log.d(TAG, "Movie with id ${movie.movieId} is clicked")
        movieViewModel.movieSelected(movie.movieId)
        movieViewModel.getImagesList(movie.movieId)
        movieViewModel.getStaffInfo(movie.movieId)
        movieViewModel.getActorsInfo(movie.movieId)
        movieViewModel.getSimilarMovies(movie.movieId)
        movieViewModel.getSeriesInfo(movie.movieId)
        movieViewModel.getMovieFromDataBaseById(movie.movieId)
        findNavController().navigate(R.id.action_navigation_profile_to_movieDetailsFragment)
    }


    private fun onClearInterestingClick() {
        movieViewModel.onCleanInterestingClick()
        interestingRecyclerView.isVisible = false
    }

    private fun onInterestingItemClick(movie: Movie) {
        Log.d(TAG, "Movie with id ${movie.movieId} is clicked")
        movieViewModel.movieSelected(movie.movieId)
        movieViewModel.getImagesList(movie.movieId)
        movieViewModel.getStaffInfo(movie.movieId)
        movieViewModel.getActorsInfo(movie.movieId)
        movieViewModel.getSimilarMovies(movie.movieId)
        movieViewModel.getSeriesInfo(movie.movieId)
        movieViewModel.getMovieFromDataBaseById(movie.movieId)
        findNavController().navigate(R.id.action_navigation_profile_to_movieDetailsFragment)
    }

    private fun extendWatched() {
        findNavController().navigate(R.id.action_navigation_profile_to_watchedFragment)
    }

    private fun extendInteresting() {
        findNavController().navigate(R.id.action_navigation_profile_to_interestingFragment)
    }

    private fun createCollection(list: List<CustomCollection>) {

        movieViewModel.getCustomCollectionNames(list)

        val dialog = Dialog(requireContext())
        val dialogView = layoutInflater.inflate(R.layout.alert_dialog, null)
        val collectionTitleInputField =
            dialogView.findViewById<AppCompatEditText>(R.id.collection_title_input_field)
        val closeButton = dialogView.findViewById<AppCompatImageButton>(R.id.close_button)
        val doneButton = dialogView.findViewById<AppCompatButton>(R.id.done_button)

        closeButton.setOnClickListener {
            dialog.dismiss()
        }

        collectionTitleInputField.doOnTextChanged { text, _, _, _ ->
            if (text != null) {
                if (text.count() >= 3) {
                    doneButton.isActivated = true
                    doneButton.isClickable = true
                    doneButton.isEnabled = true
                } else {
                    doneButton.isActivated = false
                    doneButton.isClickable = false
                    doneButton.isEnabled = false
                }
            }
        }

        doneButton.setOnClickListener {


            val collectionNameInput = collectionTitleInputField.text
            val collectionNameFormatted = collectionNameInput.toString()
                .trim { it <= ' ' }
                .lowercase(Locale.ROOT)
                .replaceFirstChar { it.uppercaseChar() }

            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                val list = movieViewModel.customCollectionNamesList.value
                if (!list.all { it != collectionNameFormatted }) {
                    Log.d(TAG, "Dialog dismiss error")
                    dialog.dismiss()
                    showErrorWarning()
                }
            }


            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                val list = movieViewModel.customCollectionNamesList.value

                if (list.all { it != collectionNameFormatted }) {
                    movieViewModel.addMovieToCustomCollection(
                        collectionNameFormatted,
                        0
                    )

                    val customCollectionView =
                        layoutInflater.inflate(
                            R.layout.custom_collection_in_profile,
                            null
                        )

                    customCollectionView.id = View.generateViewId()

                    dialog.dismiss()
                }

            }
        }

        dialog.setContentView(dialogView)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun showErrorWarning() {
        val popupWindow = ErrorFragment()
        popupWindow.setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogTheme)
        popupWindow.enterTransition = com.google.android.material.R.id.animateToStart
        popupWindow.exitTransition = com.google.android.material.R.id.animateToEnd
        popupWindow.show(requireActivity().supportFragmentManager, "ERROR")
    }

    private fun onDeleteCollectionButtonClick(collectionName: String) {
        movieViewModel.onDeleteCollectionButtonClick(collectionName)
    }

    private fun onCollectionItemClick(customCollection: CustomCollection) {
        Log.d(TAG, "Collection with name ${customCollection.collectionName} is clicked")
        movieViewModel.onCustomCollectionClick(customCollection)
        movieViewModel.chooseCollection(Collections.Custom)
        findNavController().navigate(R.id.action_navigation_profile_to_profileCollectionFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}