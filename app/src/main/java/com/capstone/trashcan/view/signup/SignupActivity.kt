package com.capstone.trashcan.view.signup

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.capstone.trashcan.R
import com.capstone.trashcan.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var signupViewModel: SignupViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setupView()
        setupAction()
        setMyButtonEnable()

        val passwordEditText = binding.passwordEditText
        val emailEditText = binding.emailEditText

        passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        emailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {

            }
        })


        signupViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(SignupViewModel::class.java)

        signupViewModel.isLoading.observe(this) {
            showLoading(it)
        }
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

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            signupViewModel.registerUser(name, email, password) { response, errorMessage ->
                response?.let {
                    // Show success dialog
                    AlertDialog.Builder(this).apply {
                        setTitle("Yeah!")
                        setMessage("Akun dengan $email sudah jadi nih. Yuk, login untuk menggunakan TRASHCAN.")
                        setPositiveButton("Login") { _, _ ->
                            finish()
                        }
                        create()
                        show()
                    }
                } ?: run {
                    // Show failure dialog with error message from the API response
                    AlertDialog.Builder(this).apply {
                        setTitle("Registration Failed")
                        setMessage(
                            errorMessage
                                ?: "There was an error while creating your account. Please try again."
                        )
                        setPositiveButton("OK") { dialog, _ ->
                            dialog.dismiss()
                        }
                        create()
                        show()
                    }
                }
            }

        }
    }

    private fun setMyButtonEnable() {
        val resultPassword = binding.passwordEditText.text
        val resultEmail = binding.emailEditText.text
        val resultName = binding.nameEditText.text
        binding.signupButton.isEnabled = resultPassword != null && resultPassword.toString().isNotEmpty() && resultEmail != null && resultEmail.toString().isNotEmpty() && resultName.toString().isNotEmpty()
    }

    private fun showLoading(state: Boolean) { binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE }
}