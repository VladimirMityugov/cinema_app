package com.example.skillcinemaapp.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.example.skillcinemaapp.R
import com.example.skillcinemaapp.databinding.FragmentOnboardingSingleBinding
import com.example.skillcinemaapp.presentation.utility.OnboardingContent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingSingleFragment : Fragment() {

    private var _binding: FragmentOnboardingSingleBinding? = null
    private val binding get() = _binding!!

    private lateinit var onboardingScreenText: AppCompatTextView
    private lateinit var onboardingScreenPicture: AppCompatImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingSingleBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val position = arguments?.getInt(POSITION)

        onboardingScreenText = binding.onboardingScreenText
        onboardingScreenPicture = binding.onboardingScreenPicture

        when (position) {
            0 -> {
                onboardingScreenText.text = buildString {
                    append(requireActivity().getString(R.string.learn))
                    append("\n")
                    append(requireActivity().getString(R.string.about_premiers))
                }
            }
            1 -> {
                onboardingScreenText.text = buildString {
                    append(requireActivity().getString(R.string.create))
                    append("\n")
                    append(requireActivity().getString(R.string.collections))
                }
            }
            2 -> {
                onboardingScreenText.text = buildString {
                    append(requireActivity().getString(R.string.share))
                    append("\n")
                    append(requireActivity().getString(R.string.with_friends))
                }
            }
        }
        onboardingScreenPicture.setImageResource(OnboardingContent.images[position!!])

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        var POSITION = "position"

        @JvmStatic
        fun newInstance(position: Int) = OnboardingSingleFragment().apply {
            arguments = Bundle().apply {
                putInt(POSITION, position)
            }
        }
    }
}
