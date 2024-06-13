package com.capstone.trashcan.view.classify

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.capstone.trashcan.R
import com.capstone.trashcan.databinding.ActivityUploadBinding
import com.capstone.trashcan.view.uriToFile
import com.yalantis.ucrop.UCrop
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class UploadActivity : AppCompatActivity() {
    private lateinit var classificationViewModel: ClassificationViewModel

    private lateinit var binding: ActivityUploadBinding

    private var currentImageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.galleryButton.setOnClickListener { startGallery() }

        classificationViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(ClassificationViewModel::class.java)

        binding.classifyButton.setOnClickListener {
            currentImageUri?.let {
                uploadImageActivity()
            } ?: run {
                showToast("Insert image first.")
            }
        }

        setupView()
        setupAction()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        currentImageUri?.let { outState.putParcelable("currentImageUri", it) }
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
//        binding.uploadButton.setOnClickListener {
//            val description = binding.descriptionEditText.text.toString()
//            uploadImageActivity(description)
//        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }


    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            startCrop(uri)
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == UCrop.REQUEST_CROP) {
                val resultUri = UCrop.getOutput(data!!)
                currentImageUri = resultUri
                showImage()
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            val error = UCrop.getError(data!!)
            showToast("Error: $error")
        }
    }

    private fun startCrop(uri: Uri) {
        val originalFileName = uri.lastPathSegment

        val croppedFileName = "$originalFileName-${System.currentTimeMillis()}.jpg"
        val destinationUri = Uri.fromFile(File(cacheDir, croppedFileName))
        val maxWidth = 800
        val maxHeight = 600

        UCrop.of(uri, destinationUri)
            .withAspectRatio(16F, 9F)
            .withMaxResultSize(maxWidth, maxHeight)
            .start(this)
    }

    private fun uploadImageActivity() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this)
            Log.d("Image File", "showImage: ${imageFile.path}")

            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "file",
                imageFile.name,
                requestImageFile
            )

            classificationViewModel.uploadImage(multipartBody) { response, errorMessage ->
                response?.let {
                    AlertDialog.Builder(this).apply {
                        setTitle("Success")
                        setMessage("Story uploaded successfully.")
                        setPositiveButton("OK") { _, _ ->
                            val intent = Intent(this@UploadActivity, ResultActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            intent.putExtra(ResultActivity.EXTRA_IMAGE_URI, currentImageUri.toString())
                            intent.putExtra(ResultActivity.EXTRA_MAIN_CATEGORY, response.mainCategory)
                            intent.putExtra(ResultActivity.EXTRA_SUB_CATEGORY, response.subCategory)
                            startActivity(intent)
                        }
                        create()
                        show()
                    }

                } ?: run {
                    AlertDialog.Builder(this).apply {
                        setTitle("Upload Failed")
                        setMessage(
                            errorMessage
                                ?: "There was an error while uploading your image. Please try again."
                        )
                        setPositiveButton("OK") { dialog, _ ->
                            dialog.dismiss()
                        }
                        create()
                        show()
                    }
                }
            }

        } ?: showToast("Image is still empty")
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}