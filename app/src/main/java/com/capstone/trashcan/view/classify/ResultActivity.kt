package com.capstone.trashcan.view.classify

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.capstone.trashcan.R
import com.capstone.trashcan.databinding.ActivityResultBinding
import com.capstone.trashcan.databinding.ActivityUploadBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        showResults()
    }

    private fun showResults() {
        val imageUri = Uri.parse(intent.getStringExtra(EXTRA_IMAGE_URI))
        imageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.imgDetail.setImageURI(it)
        }

        val mainCategory = intent.getStringExtra(EXTRA_MAIN_CATEGORY)
        binding.tvFirst.text = mainCategory

        val subCategory = intent.getStringExtra(EXTRA_SUB_CATEGORY)
        binding.tvSecond.text = subCategory

    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_MAIN_CATEGORY = "extra_main_category"
        const val EXTRA_SUB_CATEGORY = "extra_sub_category"
    }
}