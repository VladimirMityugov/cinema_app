package com.example.skillcinemaapp.ui.onboarding

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.lifecycleScope
import com.example.skillcinemaapp.R
import com.example.skillcinemaapp.databinding.FragmentLoaderBinding
import com.example.skillcinemaapp.presentation.utility.ConnectivityObserver
import com.example.skillcinemaapp.presentation.utility.ConnectivityStatus
import com.example.skillcinemaapp.presentation.utility.InternetConnectivityObserver
import com.example.skillcinemaapp.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest


@AndroidEntryPoint
class LoaderFragment : Fragment() {

    private var _binding: FragmentLoaderBinding? = null
    private val binding get() = _binding!!
    private lateinit var loader: AppCompatImageView
    private lateinit var connectivityObserver: ConnectivityObserver

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoaderBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loader = binding.loader
        connectivityObserver = InternetConnectivityObserver(requireContext())

        val animation = RotateAnimation(
            0.0f, 360.0f,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
            0.5f
        )
        animation.interpolator = LinearInterpolator()
        animation.repeatCount = Animation.INFINITE
        animation.duration = 1500
        loader.startAnimation(animation)



        viewLifecycleOwner.lifecycleScope.launchWhenStarted {

            connectivityObserver.observe().collectLatest { status ->
                when (status) {
                    ConnectivityStatus.AVAILABLE -> {
                        delay(3000L)
                        val intent = Intent(requireContext(), MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        requireActivity().finish()
                    }
                    ConnectivityStatus.UNAVAILABLE -> {
                        createNoConnectionDialog()
                    }
                    ConnectivityStatus.LOST -> {
                        createNoConnectionDialog()
                    }
                }
            }
        }
    }

    private fun createNoConnectionDialog() {
        val dialog = Dialog(requireContext(), R.style.myCustomDialog)
        val dialogView = layoutInflater.inflate(R.layout.no_connection_layout, null)

        val retryButton = dialogView.findViewById<AppCompatButton>(R.id.no_connection_button)

        retryButton.setOnClickListener {
            lifecycleScope.launchWhenStarted {
                connectivityObserver.observe().collectLatest { status ->
                    when (status) {
                        ConnectivityStatus.AVAILABLE -> {
                            delay(3000L)
                            val intent = Intent(requireContext(), MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            requireActivity().finish()
                            dialog.dismiss()
                        }
                        ConnectivityStatus.UNAVAILABLE -> {
                            Toast.makeText(requireContext(), "Нет подключения к интернету", Toast.LENGTH_SHORT)
                                .show()
                        }
                        ConnectivityStatus.LOST -> {
                            Toast.makeText(requireContext(), "Нет подключения к интернету", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
        }

        dialog.setContentView(dialogView)
        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = LoaderFragment()
    }
}
