package com.capstone.trashcan.view.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.capstone.trashcan.R
import com.capstone.trashcan.data.retrofit.ApiConfig
import com.capstone.trashcan.databinding.ActivityMainBinding
import com.capstone.trashcan.view.ViewModelFactory
import com.capstone.trashcan.view.classify.UploadActivity
import com.capstone.trashcan.view.welcome.WelcomeActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            } else {
                user.token.let { ApiConfig.setToken(it) }
            }
        }

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val navView: BottomNavigationView = binding.bottomNavigationView

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_location,
                R.id.navigation_history,
                R.id.navigation_profile
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        binding.fab.setOnClickListener {
            // Navigate to classify or perform other action
            startActivity(Intent(this, UploadActivity::class.java))

        }

        supportActionBar?.hide()

        setupAction()

        if (intent.getBooleanExtra("navigate_to_history", false)) {
            navigateToHistoryFragment()
        }

        if (intent.getBooleanExtra(EXTRA_NAVIGATE_HISTORY, false)) {
            navController.navigate(R.id.navigation_history)
        }
    }

    private fun navigateToHistoryFragment() {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navController.navigate(R.id.navigation_history) // Use the ID from your navigation graph
    }

    private fun setupAction() {

    }

    companion object {
        const val EXTRA_NAVIGATE_HISTORY = "extra_navigate_history"
    }
}