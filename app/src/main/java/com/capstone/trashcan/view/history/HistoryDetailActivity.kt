package com.capstone.trashcan.view.history

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.capstone.trashcan.R
import com.capstone.trashcan.databinding.ActivityHistoryDetailBinding
import com.capstone.trashcan.databinding.ActivityResultBinding
import com.capstone.trashcan.view.classify.ResultActivity

class HistoryDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        showResults()

        val btnBack: ImageButton = findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            finish() // Finish the current activity
        }
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

        val date = intent.getStringExtra(EXTRA_DATE)
        binding.tvTimestamp.text = date

    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_MAIN_CATEGORY = "extra_main_category"
        const val EXTRA_SUB_CATEGORY = "extra_sub_category"
        const val EXTRA_DESCRIPTION = "extra_description"
        const val EXTRA_RECOMMENDATIONS = "extra_recommendations"
        const val EXTRA_DATE = "extra_date"
    }

}