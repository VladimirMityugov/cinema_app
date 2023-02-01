package com.example.skillcinemaapp.ui.search

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.skillcinemaapp.R
import com.example.skillcinemaapp.databinding.FragmentYearSelectBinding
import com.example.skillcinemaapp.presentation.MovieViewModel
import com.example.skillcinemaapp.presentation.adapters.PeriodFromAdapter
import com.example.skillcinemaapp.presentation.adapters.PeriodToAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDate


@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
class YearSelectFragment : Fragment() {

    private var _binding: FragmentYearSelectBinding? = null
    private val binding get() = _binding!!

    private lateinit var backButton: AppCompatImageButton
    private lateinit var startPeriodRecycler: RecyclerView
    private lateinit var endPeriodRecycler: RecyclerView
    private lateinit var applyButton: AppCompatButton
    private lateinit var yearsList: List<Int>
    private lateinit var periodFromRange: AppCompatTextView
    private lateinit var periodToRange: AppCompatTextView
    private lateinit var backButtonPeriodFrom: AppCompatImageButton
    private lateinit var forwardButtonPeriodFrom: AppCompatImageButton
    private lateinit var backButtonPeriodTo: AppCompatImageButton
    private lateinit var forwardButtonPeriodTo: AppCompatImageButton


    private val movieViewModel: MovieViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentYearSelectBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backButton = binding.backButton
        startPeriodRecycler = binding.startPeriodRecycler
        endPeriodRecycler = binding.endPeriodRecycler
        applyButton = binding.applyButton
        periodFromRange = binding.periodFromRange
        periodToRange = binding.periodToRange
        backButtonPeriodFrom = binding.backButtonPeriodFrom
        forwardButtonPeriodFrom = binding.forwardButtonPeriodFrom
        backButtonPeriodTo = binding.backButtonPeriodTo
        forwardButtonPeriodTo = binding.forwardButtonPeriodTo


        yearsList = mutableListOf()
        val currentYear = LocalDate.now().year
        val startingYear = currentYear - PERIOD_RANGE
        for (i in startingYear..currentYear) {
            (yearsList as MutableList<Int>).add(i)
        }
        val yearsNumber = PERIOD_RANGE
        val fullGroupNumber = yearsNumber / 12
        var rangeStart = startingYear
        val groupsList = mutableListOf<List<Int>>()
        for (n in 0 until fullGroupNumber) {
            val group = mutableListOf<Int>()
            for (x in rangeStart..rangeStart + 11) {
                group.add(x)
            }
            groupsList.add(n, group)
            rangeStart += 12
        }

        if (yearsNumber - fullGroupNumber * 12 > 0) {
            val group = mutableListOf<Int>()
            val missingYearsNumber = yearsNumber - fullGroupNumber * 12
            for (i in rangeStart..rangeStart + missingYearsNumber) {
                group.add(i)
            }
            val index = groupsList.lastIndex + 1
            groupsList.add(index, group)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.yearFromForSearch.collectLatest { yearFrom ->
                movieViewModel.yearsOK.collectLatest { yearsOK ->
                    val periodFromAdapter = PeriodFromAdapter(
                        onYearFromClick = { item -> onYearFromClick(item) },
                        yearFrom,
                        yearsOK
                    )
                    startPeriodRecycler.adapter = periodFromAdapter
                    val chosenGroup = groupsList.find { it.contains(yearFrom) }
                    periodFromRange.text = buildString {
                        append(chosenGroup?.firstOrNull().toString())
                        append(" - ")
                        append(chosenGroup?.lastOrNull().toString())
                    }
                    val chosenIndex = groupsList.indexOf(chosenGroup)
                    val lastIndex = groupsList.lastIndex
                    var currentIndex = chosenIndex
                    periodFromAdapter.submitList(chosenGroup)

                    backButtonPeriodFrom.setOnClickListener {
                        if (currentIndex > 0) {
                            currentIndex -= 1
                            val groupToDisplay = groupsList[currentIndex]
                            periodFromAdapter.submitList(groupToDisplay)
                            val currentGroup = groupsList[currentIndex]
                            periodFromRange.text = buildString {
                                append(currentGroup.firstOrNull().toString())
                                append(" - ")
                                append(currentGroup.lastOrNull().toString())
                            }
                        }
                    }

                    forwardButtonPeriodFrom.setOnClickListener {
                        if (currentIndex < lastIndex) {
                            currentIndex += 1
                            val groupToDisplay = groupsList[currentIndex]
                            periodFromAdapter.submitList(groupToDisplay)
                            val currentGroup = groupsList[currentIndex]
                            periodFromRange.text = buildString {
                                append(currentGroup.firstOrNull().toString())
                                append(" - ")
                                append(currentGroup.lastOrNull().toString())
                            }
                        }
                    }
                }

            }
        }


        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.yearToForSearch.collectLatest { yearTo ->
                movieViewModel.yearsOK.collectLatest { yearsOK ->
                    val periodToAdapter = PeriodToAdapter(
                        onYearToClick = { item -> onYearToClick(item) },
                        yearTo,
                        yearsOK
                    )
                    endPeriodRecycler.adapter = periodToAdapter
                    val chosenGroup = groupsList.find { it.contains(yearTo) }
                    periodToRange.text = buildString {
                        append(chosenGroup?.firstOrNull().toString())
                        append(" - ")
                        append(chosenGroup?.lastOrNull().toString())
                    }
                    val chosenIndex = groupsList.indexOf(chosenGroup)
                    val lastIndex = groupsList.lastIndex
                    var currentIndex = chosenIndex
                    periodToAdapter.submitList(chosenGroup)

                    backButtonPeriodTo.setOnClickListener {
                        if (currentIndex > 0) {
                            currentIndex -= 1
                            val groupToDisplay = groupsList[currentIndex]
                            periodToAdapter.submitList(groupToDisplay)
                            val currentGroup = groupsList[currentIndex]
                            periodToRange.text = buildString {
                                append(currentGroup.firstOrNull().toString())
                                append(" - ")
                                append(currentGroup.lastOrNull().toString())
                            }
                        }
                    }

                    forwardButtonPeriodTo.setOnClickListener {
                        if (currentIndex < lastIndex) {
                            currentIndex += 1
                            val groupToDisplay = groupsList[currentIndex]
                            periodToAdapter.submitList(groupToDisplay)
                            val currentGroup = groupsList[currentIndex]
                            periodToRange.text = buildString {
                                append(currentGroup.firstOrNull().toString())
                                append(" - ")
                                append(currentGroup.lastOrNull().toString())
                            }
                        }
                    }
                }
            }
        }

        applyButton.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                movieViewModel.yearsOK.collectLatest {
                    if (it) findNavController().navigate(R.id.action_yearSelectFragment_to_searchSettingsFragment)
                    else Toast.makeText(
                        requireContext(),
                        "Проверьте верность выбранных значений",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }


        backButton.setOnClickListener {
            movieViewModel.setDefaultYears()
            findNavController().navigate(R.id.action_yearSelectFragment_to_searchSettingsFragment)
        }
    }

    private fun onYearFromClick(item: Int) {
        movieViewModel.setYearFrom(item)
        movieViewModel.checkYears()
    }

    private fun onYearToClick(item: Int) {
        movieViewModel.setYearTo(item)
        movieViewModel.checkYears()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val PERIOD_RANGE = 50
    }
}