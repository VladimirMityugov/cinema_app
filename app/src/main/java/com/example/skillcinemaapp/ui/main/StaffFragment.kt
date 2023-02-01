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
import com.example.skillcinemaapp.data.remote.staff_dto.StaffDto
import com.example.skillcinemaapp.databinding.FragmentStaffBinding
import com.example.skillcinemaapp.presentation.MovieViewModel
import com.example.skillcinemaapp.presentation.adapters.*
import com.example.skillcinemaapp.presentation.paging_sources.Persons
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest



@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
class StaffFragment : Fragment() {

    private var _binding: FragmentStaffBinding? = null
    private val binding get() = _binding!!

    private lateinit var backButton: AppCompatImageButton

    private lateinit var staffRecycler: RecyclerView

    private lateinit var movieTitle: AppCompatTextView

    private val staffAdapter = StaffAdapter(
        onStaffClick = { person -> onStaffClick(person) }
    )

    private val actorsAdapter = ActorAdapter(
        onActorClick = { actor -> onActorClick(actor) }
    )

    private val movieViewModel: MovieViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStaffBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backButton = binding.backButton
        staffRecycler = binding.staffRecyclerView
        movieTitle = binding.movieTitle

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.movieSelectedName.collectLatest {
                movieTitle.text = it
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieViewModel.personTypeChosen.collectLatest {
                when (it) {
                    Persons.Staff -> {
                        staffRecycler.adapter = staffAdapter
                        movieViewModel.staff.collectLatest {
                            staffAdapter.submitList(it)
                        }
                    }
                    Persons.Actors -> {
                        staffRecycler.adapter = actorsAdapter
                        movieViewModel.actors.collectLatest {
                            actorsAdapter.submitList(it)
                        }
                    }
                }
            }
        }

        backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun onStaffClick(person: StaffDto) {
        movieViewModel.getPersonInfo(person.staffId)
        findNavController().navigate(R.id.action_staffFragment_to_personFragment)
    }

    private fun onActorClick(actor: StaffDto) {
        movieViewModel.getPersonInfo(actor.staffId)
        findNavController().navigate(R.id.action_staffFragment_to_personFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
