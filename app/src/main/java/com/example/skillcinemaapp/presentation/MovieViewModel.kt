package com.example.skillcinemaapp.presentation

import android.app.Application
import android.net.NetworkCapabilities.*
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.skillcinemaapp.data.local.entities.*
import com.example.skillcinemaapp.data.remote.person_dto.PersonDto
import com.example.skillcinemaapp.data.remote.custom_selection_dto.ItemCustom
import com.example.skillcinemaapp.data.remote.filters_dto.CountryFilters
import com.example.skillcinemaapp.data.remote.filters_dto.GenreFilters
import com.example.skillcinemaapp.data.remote.images_dto.Image
import com.example.skillcinemaapp.data.remote.images_dto.ImagesDto
import com.example.skillcinemaapp.data.remote.movie_info_dto.MovieDto
import com.example.skillcinemaapp.data.remote.person_dto.PersonFilm
import com.example.skillcinemaapp.data.remote.popular_dto.Film
import com.example.skillcinemaapp.data.remote.premiers_dto.Item
import com.example.skillcinemaapp.data.remote.series_info_dto.Series
import com.example.skillcinemaapp.data.remote.series_info_dto.SeriesInfoDto
import com.example.skillcinemaapp.data.remote.similar_movies.SimilarMovie
import com.example.skillcinemaapp.data.remote.staff_dto.StaffDto
import com.example.skillcinemaapp.domain.UseCaseLocal
import com.example.skillcinemaapp.domain.UseCaseRemote
import com.example.skillcinemaapp.presentation.paging_sources.*
import com.example.skillcinemaapp.presentation.utility.*
import com.example.skillcinemaapp.presentation.utility.Collections
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import javax.inject.Inject
import kotlin.random.Random
import kotlin.time.Duration.Companion.days


private const val TAG = "VIEW_MODEL"
private const val DATE_FORMAT = "yyyy-MM-dd"

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class MovieViewModel @Inject constructor(
    private val useCaseRemote: UseCaseRemote,
    private val useCaseLocal: UseCaseLocal,
    app: Application
) : AndroidViewModel(app) {

    // Variables

    private val _premiers = MutableStateFlow<List<Item>>(emptyList())
    val premiers = _premiers.asStateFlow()

    private val _firstCustom = MutableStateFlow<List<ItemCustom>>(emptyList())
    val firstCustom = _firstCustom.asStateFlow()

    private val _secondCustom = MutableStateFlow<List<ItemCustom>>(emptyList())
    val secondCustom = _secondCustom.asStateFlow()

    private val _series = MutableStateFlow<List<ItemCustom>>(emptyList())
    val series = _series.asStateFlow()

    private val _top = MutableStateFlow<List<Film>>(emptyList())
    val top = _top.asStateFlow()

    private val _popular = MutableStateFlow<List<Film>>(emptyList())
    val popular = _popular.asStateFlow()

    private val _loadingState = MutableStateFlow<LoadingState>(LoadingState.IsLoading)
    val loadingState = _loadingState.asStateFlow()

    private val _countries = MutableStateFlow<List<CountryFilters>>(emptyList())
    val countries = _countries.asStateFlow()

    private val _genres = MutableStateFlow<List<GenreFilters>>(emptyList())
    val genres = _genres.asStateFlow()

    private val _selectionChosen = MutableStateFlow<Selections>(Selections.Premiers)
    val selectionChosen = _selectionChosen.asStateFlow()

    private val _personTypeChosen = MutableStateFlow<Persons>(Persons.Staff)
    val personTypeChosen = _personTypeChosen.asStateFlow()

    private val _movieSelected = MutableStateFlow(0)
    val movieSelected = _movieSelected.asStateFlow()

    private val _movieSelectedName = MutableStateFlow("")
    val movieSelectedName = _movieSelectedName.asStateFlow()

    private val _staff = MutableStateFlow<List<StaffDto>>(emptyList())
    val staff = _staff.asStateFlow()

    private val _actors = MutableStateFlow<List<StaffDto>>(emptyList())
    val actors = _actors.asStateFlow()

    private val _person = MutableStateFlow<PersonDto?>(null)
    val person = _person.asStateFlow()

    private val _movieInfo = MutableStateFlow<MovieDto?>(null)
    val movieInfo = _movieInfo.asStateFlow()

    private val _images = MutableStateFlow<ImagesDto?>(null)
    val images = _images.asStateFlow()

    private val _imageSelected = MutableStateFlow<Image?>(null)
    val imageSelected = _imageSelected.asStateFlow()

    private val _seriesInfo = MutableStateFlow<SeriesInfoDto?>(null)
    val seriesInfo = _seriesInfo.asStateFlow()

    private val _similar = MutableStateFlow<List<SimilarMovie>>(emptyList())
    val similar = _similar.asStateFlow()

    private val _countryFirst = MutableStateFlow<Int?>(null)
    val countryValueFirst = _countryFirst.asStateFlow()

    private val _genreFirst = MutableStateFlow<Int?>(null)
    val genreValueFirst = _genreFirst.asStateFlow()

    private val _countrySecond = MutableStateFlow<Int?>(null)
    val countryValueSecond = _countrySecond.asStateFlow()

    private val _genreSecond = MutableStateFlow<Int?>(null)
    val genreValueSecond = _genreSecond.asStateFlow()

    private val _addedToFavorites = MutableStateFlow(false)
    val addedToFavorites = _addedToFavorites.asStateFlow()

    private val _addedToWatch = MutableStateFlow(false)
    val addedToWatch = _addedToWatch.asStateFlow()

    private val _addedToWatched = MutableStateFlow(false)
    val addedToWatched = _addedToWatched.asStateFlow()

    private val _movieById = MutableStateFlow<Movie?>(null)
    val movieById = _movieById.asStateFlow()

    private val _episodesBySeasonNumber = MutableStateFlow<List<Series>>(emptyList())
    val episodesBySeasonNumber = _episodesBySeasonNumber.asStateFlow()

    private val _moviesByProfession = MutableStateFlow<List<PersonFilm>>(emptyList())
    val moviesByProfession = _moviesByProfession.asStateFlow()

    private val _bestPersonMovies = MutableStateFlow<List<PersonFilm>>(emptyList())
    val bestPersonMovies = _bestPersonMovies.asStateFlow()

    private val _addedToCustomCollection = MutableStateFlow(emptyMap<String, Boolean>())
    val addedToCustomCollection = _addedToCustomCollection.asStateFlow()

    private val _customCollectionNamesList = MutableStateFlow<List<String>>(emptyList())
    val customCollectionNamesList = _customCollectionNamesList

    private val _interestingList = MutableStateFlow<List<Movie>>(emptyList())
    val interestingList = _interestingList.asStateFlow()

    private val _watchedList = MutableStateFlow<List<Movie>>(emptyList())
    val watchedList = _watchedList.asStateFlow()

    private val _watchedMovies = MutableStateFlow<List<Watched>>(emptyList())

    private val _favoritesList = MutableStateFlow<List<Movie>>(emptyList())
    val favoritesList = _favoritesList.asStateFlow()

    private val _toWatchList = MutableStateFlow<List<Movie>>(emptyList())
    val toWatchList = _toWatchList.asStateFlow()

    private val _customCollectionList = MutableStateFlow<List<Movie>>(emptyList())
    val customCollectionList = _customCollectionList.asStateFlow()

    private val _collectionChosen = MutableStateFlow<Collections>(Collections.Favorites)
    val collectionChosen = _collectionChosen.asStateFlow()

    private val _customCollectionChosen = MutableStateFlow<CustomCollection?>(null)
    val customCollectionChosen = _customCollectionChosen.asStateFlow()

    private val _customCollections = MutableStateFlow<List<CustomCollection>>(emptyList())
    val customCollections = _customCollections.asStateFlow()

    private val _searchQuery = MutableStateFlow(DEFAULT_SEARCH_QUERY)
    val searchQuery = _searchQuery.asStateFlow()

    private val _countryForSearch = MutableStateFlow(1)
    val countryForSearch = _countryForSearch.asStateFlow()

    private val _genreForSearch = MutableStateFlow(1)
    val genreForSearch = _genreForSearch.asStateFlow()

    private val _orderForSearch = MutableStateFlow<OrderTypes>(OrderTypes.RATING)
    val orderForSearch = _orderForSearch.asStateFlow()

    private val _searchType = MutableStateFlow<SearchTypes>(SearchTypes.FILM)
    val searchType = _searchType.asStateFlow()

    private val _ratingFromForSearch = MutableStateFlow(DEFAULT_RATING_FROM)
    val ratingFromForSearch = _ratingFromForSearch.asStateFlow()

    private val _ratingToForSearch = MutableStateFlow(DEFAULT_RATING_TO)
    val ratingToForSearch = _ratingToForSearch.asStateFlow()

    private val _yearFromForSearch = MutableStateFlow(DEFAULT_YEAR_FROM)
    val yearFromForSearch = _yearFromForSearch.asStateFlow()

    private val _yearToForSearch = MutableStateFlow(DEFAULT_YEAR_TO)
    val yearToForSearch = _yearToForSearch.asStateFlow()

    private val _showWatchedAtSearchResult = MutableStateFlow(true)
    val showWatchedAtSearchResult = _showWatchedAtSearchResult.asStateFlow()

    private val _yearsOK = MutableStateFlow(true)
    val yearsOK = _yearsOK.asStateFlow()

    init {
        showMeMovies()
    }


//DataBaseQueries

    fun getAllInteresting() = useCaseLocal.getAllInterestingMovies()

    private suspend fun addMovieToInteresting(movieId: Int) {
        useCaseLocal.addMovieToInteresting(
            interesting = Interesting(
                interestingId = movieId
            )
        )
    }

    private suspend fun deleteAllMoviesFromInteresting() {
        useCaseLocal.cleanInteresting()
    }

    fun onInterestingButtonClick(movieId: Int) {
        viewModelScope.launch {
            addMovieToInteresting(movieId)
        }
    }

    fun onCleanInterestingClick() {
        viewModelScope.launch {
            deleteAllMoviesFromInteresting()
        }
    }

    fun buildInterestingList(allInteresting: List<Interesting>) {
        viewModelScope.launch {
            val interestingList = mutableListOf<Movie>()
            allInteresting.forEach { item ->
                val interestingMovie = useCaseLocal.getMovieFromDataBaseById(item.interestingId)
                interestingList.add(interestingMovie)
            }
            _interestingList.value = interestingList
        }
    }

    fun getAllMoviesFromCustomCollection() = useCaseLocal.getAllMoviesFromCustomCollection()

    suspend fun addMovieToCustomCollection(collectionName: String, movieId: Int) {
        useCaseLocal.addMovieToCustomCollection(
            customCollection = CustomCollection(
                collectionName = collectionName,
                movieId = movieId
            )
        )
    }

    private suspend fun deleteMovieFromCustomCollection(collectionName: String, movieId: Int) {
        useCaseLocal.deleteMovieFromCustomCollection(collectionName, movieId)
    }

    private suspend fun deleteCustomCollection(collectionName: String) {
        useCaseLocal.deleteCustomCollection(collectionName)
    }

    fun onDeleteCollectionButtonClick(collectionName: String) {
        viewModelScope.launch {
            deleteCustomCollection(collectionName = collectionName)
        }
    }

    fun getCustomCollectionNames(allMovies: List<CustomCollection>) {
        viewModelScope.launch {
            val names = mutableListOf<String>()
            allMovies.forEach {
                names.add(it.collectionName)
            }
            _customCollectionNamesList.value = names
        }
    }

    fun buildCustomCollectionList(allMovies: List<CustomCollection>) {
        viewModelScope.launch {
            val customCollectionList = mutableListOf<Movie>()
            _customCollectionChosen.collectLatest {
                val chosenCollection = it
                if (chosenCollection != null) {
                    val filteredList =
                        allMovies.filter { it.collectionName == chosenCollection.collectionName }
                    filteredList.forEach { item ->
                        if (item.movieId != 0) {
                            val customCollectionMovie =
                                useCaseLocal.getMovieFromDataBaseById(item.movieId)
                            customCollectionList.add(customCollectionMovie)
                        }
                    }
                }
                _customCollectionList.value = customCollectionList
            }
        }
    }

    fun getCustomCollections(allMovies: List<CustomCollection>) {
        viewModelScope.launch {
            val filteredCollections = mutableListOf<CustomCollection>()
            val emptyMovie = mutableListOf<CustomCollection>()
            allMovies.forEach { if (it.movieId == 0) emptyMovie.add(it) }
            emptyMovie.forEach { emptyCollection ->
                val groupedCollections =
                    allMovies.filter { it.collectionName == emptyCollection.collectionName }
                if (groupedCollections.size > 1) {
                    deleteMovieFromCustomCollection(emptyCollection.collectionName, 0)
                }
            }
            val names = allMovies.groupBy { it.collectionName }
            names.forEach { (t, u) ->
                if (u.contains(CustomCollection(t, 0))) {
                    filteredCollections.add(CustomCollection(t, u.size - 1))
                } else filteredCollections.add(CustomCollection(t, u.size))
            }
            _customCollections.value = filteredCollections
        }
    }

    fun onCustomCollectionClick(customCollection: CustomCollection) {
        viewModelScope.launch {
            _customCollectionChosen.value = customCollection
        }
    }

    fun checkCustomCollection(
        collectionName: String,
        movieId: Int,
        index: Int,
        collectionNumber: Int,
        allMovies: List<CustomCollection>
    ) {
        viewModelScope.launch {
            val customCollectionChosen =
                allMovies.filter { it.collectionName == collectionName }
            if (index <= collectionNumber - 1) {
                val initialStatus = _addedToCustomCollection.value.entries
                val status = mutableMapOf<String, Boolean>()
                initialStatus.forEach { status[it.key] = it.value }
                status[collectionName] = !customCollectionChosen.all { it.movieId != movieId }
                _addedToCustomCollection.value = status
            }
        }
    }

    fun onCustomCollectionButtonClick(collectionName: String, movieId: Int) {
        viewModelScope.launch {
            if (_addedToCustomCollection.value[collectionName] == false) {
                val initialStatus = _addedToCustomCollection.value.entries
                val status = mutableMapOf<String, Boolean>()
                initialStatus.forEach { status[it.key] = it.value }
                status[collectionName] = true
                _addedToCustomCollection.value = status
                addMovieToCustomCollection(collectionName, movieId)
            } else {
                val initialStatus = _addedToCustomCollection.value.entries
                val status = mutableMapOf<String, Boolean>()
                initialStatus.forEach { status[it.key] = it.value }
                status[collectionName] = false
                _addedToCustomCollection.value = status
                deleteMovieFromCustomCollection(collectionName, movieId)
            }
        }
    }

    private fun getAllMoviesFromDataBase() = useCaseLocal.getAllMovies()

    fun getMovieFromDataBaseById(movieId: Int) {
        viewModelScope.launch {
            _movieById.value = useCaseLocal.getMovieFromDataBaseById(movieId)
        }
    }

    private suspend fun addMovieToDataBase(
        movieId: Int
    ) {
        viewModelScope.launch {
            _movieInfo.collectLatest {
                val info = it
                if (info != null && info.kinopoiskId == movieId) {
                    useCaseLocal.addMovie(
                        movie = Movie(
                            movieId = movieId,
                            posterUri = info.posterUrl,
                            rating = info.ratingKinopoisk ?: info.ratingImdb
                            ?: info.ratingFilmCritics
                            ?: info.ratingRfCritics,
                            genre = info.genres?.firstOrNull()?.genre,
                            movieName = info.nameRu ?: info.nameEn ?: info.nameOriginal,
                            country = info.countries?.firstOrNull()?.country,
                            logoUrl = info.logoUrl,
                            description = info.description,
                            shortDescription = info.shortDescription,
                            filmLength = info.filmLength,
                            imdbId = info.imdbId,
                            nameEn = info.nameEn ?: info.nameOriginal,
                            ratingAgeLimits = info.ratingAgeLimits,
                            serial = info.serial,
                            shortFilm = info.shortFilm,
                            year = info.year
                        )
                    )
                }
            }
        }
    }

    fun onAddMovieToDataBase(movieId: Int) {
        viewModelScope.launch {
            addMovieToDataBase(movieId)
        }
    }

    fun getAllWatched() = useCaseLocal.getAllWatched()

    private suspend fun addToWatched(movieId: Int) {
        viewModelScope.launch {
            useCaseLocal.addToWatched(
                watched = Watched(
                    watchedId = movieId
                )
            )
        }
    }

    private suspend fun deleteFromWatched(movieId: Int) {
        viewModelScope.launch {
            useCaseLocal.deleteFromWatched(movieId)
        }
    }

    fun checkWatched(movieId: Int, allWatched: List<Watched>) {
        viewModelScope.launch {
            _addedToWatched.value = !allWatched.all { it.watchedId != movieId }
        }
    }

    fun onWatchedButtonClick(movieId: Int) {
        viewModelScope.launch {
            if (!_addedToWatched.value) {
                addToWatched(movieId)
                _addedToWatched.value = true
                _addedToWatch.value = false
                deleteFromToWatch(movieId)
            } else {
                deleteFromWatched(movieId)
                _addedToWatched.value = false
            }
        }
    }

    fun buildWatchedList(allWatched: List<Watched>) {
        viewModelScope.launch {
            val watchedList = mutableListOf<Movie>()
            allWatched.forEach { item ->
                val watchedMovie = useCaseLocal.getMovieFromDataBaseById(item.watchedId)
                watchedList.add(watchedMovie)
            }
            _watchedList.value = watchedList
        }
    }

    fun getWatchedMovies(allWatched: List<Watched>) {
        viewModelScope.launch {
            _watchedMovies.value = allWatched
        }
    }

    private suspend fun deleteAllMoviesFromWatched() {
        useCaseLocal.cleanWatched()
    }

    fun onCleanWatchedClick() {
        viewModelScope.launch {
            deleteAllMoviesFromWatched()
        }
    }

    fun getAllFavorites() = useCaseLocal.getAllFavorites()

    private suspend fun addToFavorites(movieId: Int) {
        viewModelScope.launch {
            useCaseLocal.addToFavorites(
                favorites = Favorites(
                    favoritesId = movieId
                )
            )
        }
    }

    private suspend fun deleteFromFavorites(movieId: Int) {
        viewModelScope.launch {
            useCaseLocal.deleteFromFavorites(movieId)
        }
    }

    fun checkFavorites(movieId: Int, allFavorites: List<Favorites>) {
        viewModelScope.launch {
            _addedToFavorites.value = !allFavorites.all { it.favoritesId != movieId }
        }
    }

    fun onFavoritesButtonClick(movieId: Int) {
        viewModelScope.launch {
            if (!_addedToFavorites.value) {
                addToFavorites(movieId)
                _addedToFavorites.value = true
            } else {
                deleteFromFavorites(movieId)
                _addedToFavorites.value = false
            }
        }
    }

    fun buildFavoritesList(allFavorites: List<Favorites>) {
        viewModelScope.launch {
            val favoritesList = mutableListOf<Movie>()
            allFavorites.forEach { item ->
                val favoriteMovie = useCaseLocal.getMovieFromDataBaseById(item.favoritesId)
                favoritesList.add(favoriteMovie)
            }
            _favoritesList.value = favoritesList
        }
    }

    fun getAllToWatch(): Flow<List<ToWatch>> = useCaseLocal.getAllToWatch()

    private suspend fun addToWatch(movieId: Int) {
        viewModelScope.launch {
            useCaseLocal.addToWatch(
                toWatch = ToWatch(
                    toWatchId = movieId
                )
            )
        }
    }

    private suspend fun deleteFromToWatch(movieId: Int) {
        viewModelScope.launch {
            useCaseLocal.deleteFromToWatch(movieId)
        }
    }

    fun checkToWatch(movieId: Int, allToWatch: List<ToWatch>) {
        viewModelScope.launch {
            _addedToWatch.value = !allToWatch.all { it.toWatchId != movieId }
        }
    }

    fun onToWatchButtonClick(movieId: Int) {
        viewModelScope.launch {
            if (!_addedToWatch.value) {
                addToWatch(movieId)
                _addedToWatch.value = true
            } else {
                deleteFromToWatch(movieId)
                _addedToWatch.value = false
            }
        }
    }

    fun buildToWatchList(allToWatch: List<ToWatch>) {
        viewModelScope.launch {
            val toWatchList = mutableListOf<Movie>()
            allToWatch.forEach { item ->
                val toWatchMovie = useCaseLocal.getMovieFromDataBaseById(item.toWatchId)
                toWatchList.add(toWatchMovie)
            }
            _toWatchList.value = toWatchList

        }
    }

//PagedSelection

    fun getSearchResult(searchQuery: String): Flow<PagingData<ItemCustom>> {
        return Pager(config = PagingConfig(20), null) {
            SearchPagingSource(
                useCaseRemote,
                countries = _countryForSearch.value,
                genres = _genreForSearch.value,
                order = when (_orderForSearch.value) {
                    OrderTypes.NUM_VOTE -> NUM_VOTE
                    OrderTypes.RATING -> RATING
                    OrderTypes.YEAR -> YEAR
                },
                type = when (_searchType.value) {
                    SearchTypes.FILM -> FILM
                    SearchTypes.ALL -> ALL
                    SearchTypes.TV_SERIES -> TV_SERIES
                },
                ratingFrom = _ratingFromForSearch.value,
                ratingTo = _ratingToForSearch.value,
                yearFrom = _yearFromForSearch.value,
                yearTo = _yearToForSearch.value,
                keyword = searchQuery
            )
        }
            .flow
            .map {
                it.map { itemCustom ->
                    itemCustom.watchedStatus = !_watchedMovies.value.all {
                        it.watchedId != itemCustom.kinopoiskId
                    }
                    itemCustom
                }
            }
            .map {
                if (!_showWatchedAtSearchResult.value) {
                    it.filter { itemCustom ->
                        itemCustom.watchedStatus == false
                    }
                } else it
            }
            .cachedIn(viewModelScope)
    }

    fun getImages(type: String?): Flow<PagingData<Image>> {
        return Pager(config = PagingConfig(20), null) {
            ImagesPagingSource(
                useCaseRemote,
                _movieSelected.value,
                type
            )
        }
            .flow.cachedIn(viewModelScope)
    }

    fun getFirstCustomPagedSelection(): Flow<PagingData<ItemCustom>> {

        return Pager(
            config = PagingConfig(20), null
        ) {
            FirstCustomSelectionPagingSource(
                useCaseRemote,
                _countryFirst.value!!,
                _genreFirst.value!!
            )
        }
            .flow
            .map {
                it.map { itemCustom ->
                    itemCustom.watchedStatus = !_watchedMovies.value.all {
                        it.watchedId != itemCustom.kinopoiskId
                    }
                    itemCustom
                }
            }
            .cachedIn(viewModelScope)
    }

    fun getSecondCustomPagedSelection(): Flow<PagingData<ItemCustom>> {
        return Pager(
            config = PagingConfig(20), null
        ) {
            SecondCustomSelectionPagingSource(
                useCaseRemote,
                _countrySecond.value!!,
                _genreSecond.value!!
            )
        }
            .flow
            .map {
                it.map { itemCustom ->
                    itemCustom.watchedStatus = !_watchedMovies.value.all {
                        it.watchedId != itemCustom.kinopoiskId
                    }
                    itemCustom
                }
            }
            .cachedIn(viewModelScope)
    }

    fun getPopular(): Flow<PagingData<Film>> {
        return Pager(
            config = PagingConfig(20), null
        ) { PopularPagingSource(useCaseRemote) }
            .flow
            .map {
                it.map { itemCustom ->
                    itemCustom.watchedStatus = !_watchedMovies.value.all {
                        it.watchedId != itemCustom.filmId
                    }
                    itemCustom
                }
            }
            .cachedIn(viewModelScope)
    }

    fun getTop(): Flow<PagingData<Film>> {
        return Pager(
            config = PagingConfig(20), null
        ) { TopPagingSource(useCaseRemote) }
            .flow
            .map {
                it.map { itemCustom ->
                    itemCustom.watchedStatus = !_watchedMovies.value.all {
                        it.watchedId != itemCustom.filmId
                    }
                    itemCustom
                }
            }
            .cachedIn(viewModelScope)
    }

    fun getSeries(): Flow<PagingData<ItemCustom>> {
        return Pager(
            config = PagingConfig(20), null
        ) { SeriesPagingSource(useCaseRemote) }
            .flow
            .map {
                it.map { itemCustom ->
                    itemCustom.watchedStatus = !_watchedMovies.value.all {
                        it.watchedId != itemCustom.kinopoiskId
                    }
                    itemCustom
                }
            }
            .cachedIn(viewModelScope)
    }

//ListSelection

    fun getStaffInfo(filmId: Int) {
        viewModelScope.launch {
            kotlin.runCatching {
                useCaseRemote.getStaffInfo(filmId).filter { it.professionKey != ACTOR }
            }.fold(
                onSuccess = { staff ->
                    if (staff.isNotEmpty()) _staff.value = staff
                    else _staff.value = emptyList()
                },
                onFailure = {
                    Log.d(TAG, "${it.message}")
                    _staff.value = emptyList()
                }
            )
        }
    }

    fun getActorsInfo(filmId: Int) {
        viewModelScope.launch {
            kotlin.runCatching {
                useCaseRemote.getStaffInfo(filmId).filter { it.professionKey == ACTOR }
            }.fold(
                onSuccess = { actors ->
                    if (actors.isNotEmpty()) _actors.value = actors
                    else _actors.value = emptyList()
                },
                onFailure = {
                    Log.d(TAG, "${it.message}")
                    _actors.value = emptyList()
                }
            )
        }
    }

    fun getPersonInfo(personId: Int) {
        viewModelScope.launch {
            _person.value = useCaseRemote.getPersonInfo(personId)
        }
    }

    fun getBestPersonMovies() {
        viewModelScope.launch {
            val bestPersonMovies = mutableListOf<PersonFilm>()
            _person.value?.films?.forEach {
                val rating = it.rating
                val convertedRating = rating?.toDoubleOrNull()
                if (convertedRating != null && convertedRating >= 6.8) bestPersonMovies.add(it)
            }
            _bestPersonMovies.value = bestPersonMovies
        }
    }

    fun getSimilarMovies(movieId: Int) {
        viewModelScope.launch {
            kotlin.runCatching {
                useCaseRemote.getSimilarMovies(movieId)
            }.fold(
                onSuccess = { movies ->
                    if (movies.isNotEmpty()) _similar.value = movies
                    else _similar.value = emptyList()
                },
                onFailure = {
                    Log.d(TAG, "${it.message}")
                }
            )
        }
    }

    fun getSeriesInfo(seriesId: Int) {
        viewModelScope.launch {
            kotlin.runCatching {
                useCaseRemote.getSeriesInfo(seriesId)
            }.fold(
                onSuccess = { info ->
                    if (info.items.isNotEmpty()) _seriesInfo.value = info
                    else _seriesInfo.value = null
                },
                onFailure = {
                    Log.d(TAG, "${it.message}")
                }
            )
        }
    }

    fun getSeriesFilter(seasonNumber: Int) {
        viewModelScope.launch {
            _episodesBySeasonNumber.value =
                _seriesInfo.value?.items?.filter { it.number == seasonNumber }!!
        }
    }

    fun getFilmographyFilter(profession: String) {
        viewModelScope.launch {
            _moviesByProfession.value =
                _person.value?.films?.filter { it.professionKey == profession.uppercase(Locale.ROOT) }!!
        }
    }

    fun getMovieInfo(movieId: Int) {
        viewModelScope.launch {
            kotlin.runCatching {
                useCaseRemote.getMovieInfo(movieId)
            }.fold(
                onSuccess = { info ->
                    _movieInfo.value = info
                },
                onFailure = {
                    Log.d(TAG, "${it.message}")
                }
            )
        }
    }

    fun getImagesList(movieId: Int) {
        viewModelScope.launch {
            kotlin.runCatching {
                useCaseRemote.getImagesList(movieId)
            }.fold(
                onSuccess = { images ->
                    if (images.items.isNotEmpty()) _images.value = images
                    else _images.value = null
                },
                onFailure = {
                    Log.d(TAG, "${it.message}")
                    _images.value = null
                }
            )
        }
    }

    fun getFirstListSelection(country: Int, genre: Int) {
        viewModelScope.launch {
            _firstCustom.value = useCaseRemote.getFirstCustomListSelection(
                country = country,
                genre = genre
            )
        }
    }


    fun getSecondListSelection(country: Int, genre: Int) {
        viewModelScope.launch {
            _secondCustom.value = useCaseRemote.getSecondCustomListSelection(
                country = country,
                genre = genre
            )
        }
    }

    private fun getSeriesListSelection() {
        viewModelScope.launch {
            _series.value = useCaseRemote.getSeriesListSelection()
        }
    }

    private fun getPopularListSelection() {
        viewModelScope.launch {
            _popular.value = useCaseRemote.getPopularListSelection()
        }
    }

    private fun getTopListSelection() {
        viewModelScope.launch {
            _top.value = useCaseRemote.getTopListSelection()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getPremiers() {
        viewModelScope.launch {
            val simpleDateFormat = SimpleDateFormat(
                DATE_FORMAT,
                Locale.getDefault()
            )

            val itemsToDisplay = mutableListOf<Item>()

            val calendar = Calendar.getInstance()
            val currentDate = LocalDate.now()
            val currentYear = calendar.get(Calendar.YEAR)
            val currentMonth = currentDate.month.name.uppercase(Locale.ROOT)
            val currentDay = currentDate.dayOfMonth

            val remainingDaysTillMonthEnd = calendar.get(Calendar.MONTH.days.inWholeDays.toInt()) - currentDay

            if (remainingDaysTillMonthEnd < 14) {
                val premiersForCurrentMonth = useCaseRemote.getPremiers(currentYear, currentMonth)
                Log.d(TAG, "premiers for current month: $premiersForCurrentMonth")
                premiersForCurrentMonth.forEach { movie ->
                    val premiereDate = simpleDateFormat.parse(movie.premiereRu)
                    if (premiereDate != null) {
                        calendar.time = premiereDate
                    }
                    if (calendar.get(Calendar.DAY_OF_MONTH) in currentDay..currentDay + remainingDaysTillMonthEnd) {
                        itemsToDisplay.add(movie)
                    }
                }
                if (currentDate.monthValue <= 11) {
                    val nextMonthNumber = currentDate.monthValue + 1
                    val nextMonthName = getMonthByNumber(nextMonthNumber)
                    val premiersForNextMonth = useCaseRemote.getPremiers(currentYear, nextMonthName)
                    Log.d(TAG, "premiers for next month: $premiersForNextMonth")
                    premiersForNextMonth.forEach { movie ->
                        val premiereDate = simpleDateFormat.parse(movie.premiereRu)
                        if (premiereDate != null) {
                            calendar.time = premiereDate
                        }
                        if (calendar.get(Calendar.DAY_OF_MONTH) in 1..14 - remainingDaysTillMonthEnd) {
                            itemsToDisplay.add(movie)
                        }
                    }
                } else {
                    val premiersForNextMonth = useCaseRemote.getPremiers(currentYear + 1, "JANUARY")
                    premiersForNextMonth.forEach { movie ->
                        val premiereDate = simpleDateFormat.parse(movie.premiereRu)
                        if (premiereDate != null) {
                            calendar.time = premiereDate
                        }
                        if (calendar.get(Calendar.DAY_OF_MONTH) in 1..14 - remainingDaysTillMonthEnd) {
                            itemsToDisplay.add(movie)
                        }
                    }
                }
            } else {
                val premiersForCurrentMonth = useCaseRemote.getPremiers(currentYear, currentMonth)
                premiersForCurrentMonth.forEach { movie ->
                    val premiereDate = simpleDateFormat.parse(movie.premiereRu)
                    if (premiereDate != null) {
                        calendar.time = premiereDate
                    }
                    if (calendar.get(Calendar.DAY_OF_MONTH) in currentDay..currentDay + 14) {
                        itemsToDisplay.add(movie)
                    }
                }
            }
            _premiers.value = itemsToDisplay
        }
    }


    //Auxiliary

    fun updateBestPersonMoviesPosters() {
        viewModelScope.launch {
            _bestPersonMovies.collectLatest {
                getAllMoviesFromDataBase().collectLatest { movies ->
                    _bestPersonMovies.value.forEach { item ->
                        if (!movies.all { it.movieId != item.filmId }) {
                            item.posterUri = movies.find { it.movieId == item.filmId }?.posterUri
                        } else {
                            item.posterUri =
                                "https://kinopoiskapiunofficial.tech/images/posters/kp/${item.filmId}.jpg"
                        }
                    }
                }
            }
        }
    }

    fun updateFilmographyMoviesPosters() {
        viewModelScope.launch {
            _person.collectLatest {
                getAllMoviesFromDataBase().collectLatest { movies ->
                    _person.value?.films?.forEach { item ->
                        if (!movies.all { it.movieId != item.filmId }) {
                            item.posterUri = movies.find { it.movieId == item.filmId }?.posterUri
                        } else {
                            item.posterUri =
                                "https://kinopoiskapiunofficial.tech/images/posters/kp/${item.filmId}.jpg"
                        }
                    }
                }
            }
        }
    }

    fun updatePremiersWatchStatus() {
        viewModelScope.launch {
            _premiers.collectLatest {
                getAllWatched().collectLatest { listWatched ->
                    _premiers.value.forEach { item ->
                        if (!listWatched.all { it.watchedId != item.kinopoiskId }) item.watched_status =
                            true
                    }
                }
            }
        }
    }

    fun updateFirstCustomWatchStatus() {
        viewModelScope.launch {
            _firstCustom.collectLatest {
                getAllWatched().collectLatest { listWatched ->
                    _firstCustom.value.forEach { item ->
                        if (!listWatched.all { it.watchedId != item.kinopoiskId }) item.watchedStatus =
                            true
                    }
                }
            }
        }
    }

    fun updateSecondCustomWatchStatus() {
        viewModelScope.launch {
            _secondCustom.collectLatest {
                getAllWatched().collectLatest { listWatched ->
                    _secondCustom.value.forEach { item ->
                        if (!listWatched.all { it.watchedId != item.kinopoiskId }) item.watchedStatus =
                            true
                    }
                }
            }
        }
    }

    fun updatePopularWatchStatus() {
        viewModelScope.launch {
            _popular.collectLatest {
                getAllWatched().collectLatest { listWatched ->
                    _popular.value.forEach { item ->
                        if (!listWatched.all { it.watchedId != item.filmId }) item.watchedStatus =
                            true
                    }
                }
            }
        }
    }

    fun updateTopWatchStatus() {
        viewModelScope.launch {
            _top.collectLatest {
                getAllWatched().collectLatest { listWatched ->
                    _top.value.forEach { item ->
                        if (!listWatched.all { it.watchedId != item.filmId }) item.watchedStatus =
                            true
                    }
                }
            }
        }
    }

    fun updateSeriesWatchStatus() {
        viewModelScope.launch {
            _series.collectLatest {
                getAllWatched().collectLatest { listWatched ->
                    _series.value.forEach { item ->
                        if (!listWatched.all { it.watchedId != item.kinopoiskId }) item.watchedStatus =
                            true
                    }
                }
            }
        }
    }

    fun showWatchedMoviesAtSearchResult() {
        viewModelScope.launch {
            _showWatchedAtSearchResult.value = !_showWatchedAtSearchResult.value
        }
    }

    fun onSearchTypeClick(searchType: String) {
        viewModelScope.launch {
            _searchType.value = when (searchType) {
                ALL -> SearchTypes.ALL
                FILM -> SearchTypes.FILM
                TV_SERIES -> SearchTypes.TV_SERIES
                else -> SearchTypes.FILM
            }
        }
    }

    fun onSearchOrderClick(searchOrder: String) {
        viewModelScope.launch {
            _orderForSearch.value = when (searchOrder) {
                RATING -> OrderTypes.RATING
                NUM_VOTE -> OrderTypes.NUM_VOTE
                YEAR -> OrderTypes.YEAR
                else -> OrderTypes.RATING
            }
        }
    }

    fun setRatingForSearch(minValue: Int, maxValue: Int) {
        viewModelScope.launch {
            _ratingFromForSearch.value = minValue
            _ratingToForSearch.value = maxValue
        }
    }

    fun setCountryForSearch(countryId: Int) {
        viewModelScope.launch {
            _countryForSearch.value = countryId
        }
    }

    fun setYearFrom(yearFrom: Int) {
        viewModelScope.launch {
            _yearFromForSearch.value = yearFrom
        }
    }

    fun setYearTo(yearTo: Int) {
        viewModelScope.launch {
            _yearToForSearch.value = yearTo
        }
    }

    fun setDefaultYears() {
        viewModelScope.launch {
            _yearFromForSearch.value = DEFAULT_YEAR_FROM
            _yearToForSearch.value = DEFAULT_YEAR_TO
        }
    }

    fun setGenreForSearch(genreId: Int) {
        viewModelScope.launch {
            _genreForSearch.value = genreId
        }
    }

    fun checkYears() {
        viewModelScope.launch {
            _yearsOK.value = _yearFromForSearch.value < _yearToForSearch.value
        }
    }


    private fun getFilters() {
        viewModelScope.launch {
            if (_countries.value == emptyList<CountryFilters>()
                && _genres.value == emptyList<GenreFilters>()
            ) {
                _countries.value = useCaseRemote.getFilters().countries
                _countryFirst.value = Random.nextInt(1, 7)
                _countrySecond.value = Random.nextInt(7, 15)
                _genres.value = useCaseRemote.getFilters().genres
                _genreFirst.value = Random.nextInt(1, 5)
                _genreSecond.value = Random.nextInt(5, 10)
            } else {
                _countryFirst.value = Random.nextInt(1, 7)
                _countrySecond.value = Random.nextInt(7, 15)
                _genreFirst.value = Random.nextInt(1, 5)
                _genreSecond.value = Random.nextInt(5, 10)
            }
        }
    }

    fun onSearchQueryInput(query: String) {
        viewModelScope.launch {
            _searchQuery.value = query
        }
    }

    fun resetSearchQuery() {
        viewModelScope.launch {
            _searchQuery.value = DEFAULT_SEARCH_QUERY
        }
    }

    private fun getMonthByNumber(monthNumber: Int): String {
        val c = Calendar.getInstance()
        val monthDate = SimpleDateFormat("MMMM", Locale.US)
        c[Calendar.MONTH] = monthNumber - 1
        return monthDate.format(c.time).uppercase(Locale.ROOT)
    }

    fun chooseSelection(selection: Selections) {
        viewModelScope.launch {
            _selectionChosen.value = selection
        }
    }

    fun chooseCollection(collections: Collections) {
        viewModelScope.launch {
            _collectionChosen.value = collections
        }
    }

    fun choosePersonType(person: Persons) {
        viewModelScope.launch {
            _personTypeChosen.value = person
        }
    }

    fun movieSelected(itemId: Int) {
        viewModelScope.launch {
            _movieSelected.value = itemId
        }
    }

    fun imageSelected(image: Image) {
        viewModelScope.launch {
            _imageSelected.value = image
        }
    }

    fun movieSelectedName(itemName: String) {
        viewModelScope.launch {
            _movieSelectedName.value = itemName
        }
    }

    private fun showMeMovies() {
        viewModelScope.launch {
            _loadingState.value = LoadingState.IsLoading
            getFilters()
            getPremiers()
            getTopListSelection()
            getPopularListSelection()
            getSeriesListSelection()
        }
    }

    fun stopLoading() {
        viewModelScope.launch {
            _loadingState.value = LoadingState.LoadingIsFinished
        }
    }

    companion object {
        private const val ACTOR = "ACTOR"
        private const val RATING = "RATING"
        private const val NUM_VOTE = "NUM_VOTE"
        private const val YEAR = "YEAR"
        private const val ALL = "ALL"
        private const val FILM = "FILM"
        private const val TV_SERIES = "TV_SERIES"
        private const val DEFAULT_YEAR_FROM = 2000
        private const val DEFAULT_YEAR_TO = 2023
        private const val DEFAULT_SEARCH_QUERY = ""
        private const val DEFAULT_RATING_FROM = 7
        private const val DEFAULT_RATING_TO = 10
    }
}




