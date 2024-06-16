package com.capstone.trashcan.view.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.capstone.trashcan.R
import com.capstone.trashcan.data.retrofit.ApiConfig
import com.capstone.trashcan.databinding.ActivityMainBinding
import com.capstone.trashcan.view.ViewModelFactory
import com.capstone.trashcan.view.classify.UploadActivity
import com.capstone.trashcan.view.welcome.WelcomeActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

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

//        auth = Firebase.auth
//        val firebaseUser = auth.currentUser
//        if (firebaseUser == null) {
//            // Not signed in, launch the Login activity
//            startActivity(Intent(this, WelcomeActivity::class.java))
//            finish()
//            return
//        }

//        val navView: BottomAppBar = binding.bottomAppBar
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
    }

    private fun setupAction() {
        binding.logoutButton.setOnClickListener {
//            startActivity(Intent(this, WelcomeActivity::class.java))
            signOut()
        }

//        binding.signupButton.setOnClickListener {
//            startActivity(Intent(this, SignupActivity::class.java))
//        }
    }

    private fun signOut() {
        viewModel.logout()
//        lifecycleScope.launch {
//            val credentialManager = CredentialManager.create(this@MainActivity)
//            auth.signOut()
//            credentialManager.clearCredentialState(ClearCredentialStateRequest())
//            startActivity(Intent(this@MainActivity, WelcomeActivity::class.java))
//            finish()
//        }

    }
}