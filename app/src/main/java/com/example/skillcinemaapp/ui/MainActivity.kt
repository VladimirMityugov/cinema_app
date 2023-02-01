package com.example.skillcinemaapp.ui


import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.skillcinemaapp.R
import com.example.skillcinemaapp.databinding.ActivityMainBinding
import com.example.skillcinemaapp.presentation.utility.ConnectivityObserver
import com.example.skillcinemaapp.presentation.utility.ConnectivityStatus
import com.example.skillcinemaapp.presentation.utility.InternetConnectivityObserver
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest



@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var connectivityObserver: ConnectivityObserver


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        connectivityObserver = InternetConnectivityObserver(applicationContext)

        val navView: BottomNavigationView = binding.bottomNavigationView

        supportActionBar?.hide()
        navController = findNavController(R.id.nav_host_fragment_activity_main)



        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_main, R.id.navigation_search, R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_main -> navController.navigate(R.id.action_global_navigation_main)
                R.id.navigation_profile -> navController.navigate(R.id.action_global_navigation_profile)
                R.id.navigation_search -> navController.navigate(R.id.action_global_navigation_search)
            }
            true
        }

        lifecycleScope.launchWhenStarted {
            connectivityObserver.observe().collectLatest { status ->
                when (status) {
                    ConnectivityStatus.AVAILABLE -> {
                        navController.navigate(navController.currentDestination!!.id)
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
        val dialog = Dialog(this, R.style.myCustomDialog)
        val dialogView = layoutInflater.inflate(R.layout.no_connection_layout, null)

        val retryButton = dialogView.findViewById<AppCompatButton>(R.id.no_connection_button)

        retryButton.setOnClickListener {
            lifecycleScope.launchWhenStarted {
                connectivityObserver.observe().collectLatest { status ->
                    when (status) {
                        ConnectivityStatus.AVAILABLE -> {
                            navController.navigate(navController.currentDestination!!.id)
                            dialog.dismiss()
                        }
                        ConnectivityStatus.UNAVAILABLE -> {
                            Toast.makeText(this@MainActivity, "Нет подключения к интернету", Toast.LENGTH_SHORT)
                                .show()
                        }
                        ConnectivityStatus.LOST -> {
                            Toast.makeText(this@MainActivity, "Нет подключения к интернету", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
        }

        dialog.setContentView(dialogView)
        dialog.show()
        dialog.window?.setGravity(Gravity.FILL)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

}


