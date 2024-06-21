package com.capstone.trashcan.view.classify

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.capstone.trashcan.R
import com.capstone.trashcan.data.entity.ClassificationHistoryEntity
import com.capstone.trashcan.databinding.ActivityResultBinding
import com.capstone.trashcan.view.HistoryViewModelFactory
import com.capstone.trashcan.view.ViewModelFactory
import com.capstone.trashcan.view.history.HistoryViewModel
import com.capstone.trashcan.view.main.MainActivity
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ResultActivity : AppCompatActivity() {
    private val viewModel by viewModels<HistoryViewModel> {
        HistoryViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.saveHistoryIcon.setOnClickListener {
//            viewModel.insertHistory(insertThisHistory())
            saveHistory()
        }

        supportActionBar?.hide()
        showResults()

        binding.homeButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun saveHistory() {
        val history = insertThisHistory()

        // Insert history in the background using a coroutine
        lifecycleScope.launch {
            viewModel.insertHistory(history)
            runOnUiThread {
                // Change icon to filled bookmark
                binding.saveHistoryIcon.setImageResource(R.drawable.ic_bookmark_filled)

                // Show success toast
                Toast.makeText(this@ResultActivity, "Successfully saved in history", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun insertThisHistory(): ClassificationHistoryEntity {
        val subCategory = intent.getStringExtra(EXTRA_SUB_CATEGORY)
        val mainCategory = intent.getStringExtra(EXTRA_MAIN_CATEGORY)
        val photo = Uri.parse(intent.getStringExtra(EXTRA_IMAGE_URI))
        val description = intent.getStringExtra(EXTRA_DESCRIPTION)
        val recommendations = intent.getStringArrayListExtra(EXTRA_RECOMMENDATIONS)
        val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

        return ClassificationHistoryEntity(
            subCategory = subCategory,
            mainCategory = mainCategory,
            photo = photo.toString(),
            description = description,
            recommendations = recommendations,
            date = date

        );
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

        val description = intent.getStringExtra(EXTRA_DESCRIPTION)
        binding.tvDescription.text = description

        val recommendations = intent.getStringArrayListExtra(EXTRA_RECOMMENDATIONS)
        val recommendationsText = recommendations?.joinToString(separator = "\n") { " $it" } ?: "No recommendations available"
        binding.tvRecommendation.text = recommendationsText

    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("navigate_to_history", true)
        startActivity(intent)
        super.onBackPressed()
    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_MAIN_CATEGORY = "extra_main_category"
        const val EXTRA_SUB_CATEGORY = "extra_sub_category"
        const val EXTRA_DESCRIPTION = "extra_description"
        const val EXTRA_RECOMMENDATIONS = "extra_recommendations"
    }
}