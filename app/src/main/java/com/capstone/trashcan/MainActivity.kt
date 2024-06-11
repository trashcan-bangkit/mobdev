package com.capstone.trashcan

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.capstone.trashcan.databinding.ActivityMainBinding
import com.capstone.trashcan.view.welcome.WelcomeActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        val firebaseUser = auth.currentUser
        if (firebaseUser == null) {
            // Not signed in, launch the Login activity
            startActivity(Intent(this, WelcomeActivity::class.java))
            finish()
            return
        }

        setupAction()
        displayUserInfo(firebaseUser)
    }

    private fun displayUserInfo(user: FirebaseUser) {
        // Get user's display name
        val displayName = user.displayName
        if (displayName != null) {
            binding.userName.text = displayName
        }

        // Get user's profile photo URL
        val photoUrl: Uri? = user.photoUrl
        if (photoUrl != null) {
            Glide.with(this).load(photoUrl).into(binding.imgItemPhoto)
        }
    }

    private fun setupAction() {
//        binding.logoutButton.setOnClickListener {
//            startActivity(Intent(this, WelcomeActivity::class.java))
//            signOut()
//        }

//        binding.signupButton.setOnClickListener {
//            startActivity(Intent(this, SignupActivity::class.java))
//        }
    }

//    private fun signOut() {
//
//        lifecycleScope.launch {
//            val credentialManager = CredentialManager.create(this@MainActivity)
//            auth.signOut()
//            credentialManager.clearCredentialState(ClearCredentialStateRequest())
//            startActivity(Intent(this@MainActivity, WelcomeActivity::class.java))
//            finish()
//        }
//
//    }
}