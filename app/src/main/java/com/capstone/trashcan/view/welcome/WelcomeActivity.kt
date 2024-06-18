package com.capstone.trashcan.view.welcome

import ViewPagerAdapter
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.capstone.trashcan.R
import com.capstone.trashcan.databinding.ActivityWelcomeBinding
import com.capstone.trashcan.view.login.LoginActivity
import com.capstone.trashcan.view.signup.SignupActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding
    private lateinit var auth: FirebaseAuth

    private lateinit var dots: Array<TextView?>
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

//        binding.signInButton.setOnClickListener {
//            signIn()
//        }

        setupView()
        setupAction()
        setupViewPager()
    }

    private fun setupViewPager() {
        viewPagerAdapter = ViewPagerAdapter(this)
        binding.slideViewPager.adapter = viewPagerAdapter
        setDotIndicator(0)

        binding.slideViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                setDotIndicator(position)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    private fun setDotIndicator(position: Int) {
        dots = arrayOfNulls(3)
        binding.dotIndicator.removeAllViews()

        for (i in dots.indices) {
            dots[i] = TextView(this).apply {
                text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Html.fromHtml("&#8226;", Html.FROM_HTML_MODE_LEGACY)
                } else {
                    Html.fromHtml("&#8226;")
                }
                textSize = 35f
                setTextColor(ContextCompat.getColor(this@WelcomeActivity, R.color.black))
            }
            binding.dotIndicator.addView(dots[i])
        }

        // Update the color of the dot for the selected position
        if (dots.isNotEmpty()) {
            for (i in dots.indices) {
                if (i == position) {
                    dots[i]?.setTextColor(ContextCompat.getColor(this, R.color.black))
                } else {
                    dots[i]?.setTextColor(ContextCompat.getColor(this, R.color.grey)) // Set the color of inactive dots
                }
            }
        }
    }

//    private fun signIn() {
//        val credentialManager = CredentialManager.create(this) //import from androidx.CredentialManager
//        val googleIdOption = GetGoogleIdOption.Builder()
//            .setFilterByAuthorizedAccounts(false)
////            .setServerClientId("545902229393-2at25l7kvt8a4875doakg33nrfkukpmg.apps.googleusercontent.com")
//            .setServerClientId("545902229393-2at25l7kvt8a4875doakg33nrfkukpmg.apps.googleusercontent.com")
//            .build()
//        val request = GetCredentialRequest.Builder() //import from androidx.CredentialManager
//            .addCredentialOption(googleIdOption)
//            .build()
//
//        lifecycleScope.launch {
//            try {
//                Log.d(TAG, "signInWithCredential:CEK1")
//                val result: GetCredentialResponse = credentialManager.getCredential( //import from androidx.CredentialManager
//                    request = request,
//                    context = this@WelcomeActivity,
//                )
//                handleSignIn(result)
//            } catch (e: GetCredentialException) { //import from androidx.CredentialManager
//                Log.d("Error", e.message.toString())
//            }
//        }
//    }

//    private fun handleSignIn(result: GetCredentialResponse) {
//        Log.d(TAG, "signInWithCredential:CEK2")
//        // Handle the successfully returned credential.
//        when (val credential = result.credential) {
//            is CustomCredential -> {
//                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
//                    // Process Login dengan Firebase Auth
//                    Log.d(TAG, "signInWithCredential:CEK3")
//                    try {
//                        Log.d(TAG, "signInWithCredential:CEK4")
//                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
//                        firebaseAuthWithGoogle(googleIdTokenCredential.idToken)
//                    } catch (e: GoogleIdTokenParsingException) {
//                        Log.e(TAG, "Received an invalid google id token response", e)
//                    }
//                } else {
//                    // Catch any unrecognized custom credential type here.
//                    Log.e(TAG, "Unexpected type of credential")
//                }
//            }
//            else -> {
//                // Catch any unrecognized credential type here.
//                Log.e(TAG, "Unexpected type of credential")
//            }
//        }
//    }

//    private fun firebaseAuthWithGoogle(idToken: String) {
//        val credential: AuthCredential = GoogleAuthProvider.getCredential(idToken, null)
//        auth.signInWithCredential(credential)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    Log.d(TAG, "signInWithCredential:success")
//                    val user: FirebaseUser? = auth.currentUser
//                    updateUI(user)
//                } else {
//                    Log.w(TAG, "signInWithCredential:failure", task.exception)
//                    updateUI(null)
//                }
//            }
//    }

//    private fun updateUI(currentUser: FirebaseUser?) {
//        if (currentUser != null) {
//            startActivity(Intent(this@WelcomeActivity, MainActivity::class.java))
//            finish()
//        }
//    }

    companion object {
        private const val TAG = "LoginActivity"
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

//    override fun onStart() {
//        super.onStart()
//        val currentUser = auth.currentUser
//        updateUI(currentUser)
//    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.signupButton.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }
}