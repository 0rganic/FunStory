package com.example.funstory.Ui.addStory

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.funstory.Ui.camera.Camera
import com.example.funstory.Ui.main.MainActivity
import com.example.funstory.databinding.ActivityAddStoryBinding
import com.example.funstory.utils.reduceImageFile
import com.example.funstory.utils.rotateImageFile
import com.example.funstory.utils.uriToFile
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

@Suppress("DEPRECATION")
@AndroidEntryPoint
class AddStory : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding
    private var selectedFile: File? = null
    private val addStoryViewModel: AddStoryViewModel by viewModels()

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "New Story"

        if (!areAllPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
    binding.btnCamera.setOnClickListener{ startCameraX() }
    binding.btnGallery.setOnClickListener{ startGallery() }

    binding.buttonAdd.setOnClickListener{
        if (selectedFile != null) {
            val description = binding.edtDescription.text.toString()
            val photo = processImage()
            val lat = 0.0
            val lon = 0.0
            if (description.isNotEmpty()) {
                submitStory(description, photo, lat, lon)
            } else {
                binding.edtDescription.error = "Description is Required"
            }
        } else {
            showToast("Please select a file first.")
        }
    }
}

private fun submitStory(
    description: String,
    photo: MultipartBody.Part,
    lat: Double?,
    lon: Double?
) {
    lifecycleScope.launch {
        addStoryViewModel.getToken().collect { token ->
            if (token != null) {
                val jwt = "Bearer $token"
                try {
                    addStoryViewModel.addStory(description, photo, lat, lon, jwt).collect { result ->
                        if (result.isSuccess) {
                            showToast("Story Uploaded")
                            navigateToMainActivity()
                        } else {
                            showToast("Add Story Failed: ${result.exceptionOrNull()?.message}")
                        }
                    }
                } catch (e: Exception) {
                    showToast("Add Story Failed: ${e.message}")
                }
            }
        }
    }
}

private fun startGallery() {
    val intent = Intent(Intent.ACTION_GET_CONTENT)
    intent.type = "image/*"
    val chooser = Intent.createChooser(intent, "Choose a Picture")
    galleryLauncher.launch(chooser)
}

private val galleryLauncher = registerForActivityResult(
    ActivityResultContracts.StartActivityForResult()
) { result ->
    if (result.resultCode == RESULT_OK) {
        val selectedImg = result.data?.data as Uri
        selectedImg.let { uri ->
            val file = uriToFile(uri, this@AddStory)
            selectedFile = file
            binding.previewImageView.setImageURI(uri)
        }
    }
}

private fun startCameraX() {
    val intent = Intent(this, Camera::class.java)
    cameraLauncher.launch(intent)
}

private val cameraLauncher = registerForActivityResult(
    ActivityResultContracts.StartActivityForResult()
) { result ->
    if (result.resultCode == CAMERA_X_RESULT) {
        val file = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            result.data?.getSerializableExtra("picture") as? File
        } else {
            @Suppress("DEPRECATION")
            result.data?.getSerializableExtra("picture") as? File
        }

        val isBackCamera = result.data?.getBooleanExtra("isBackCamera", true) ?: true

        file?.let { imageFile ->
            rotateImageFile(imageFile, isBackCamera)
            selectedFile = imageFile
            binding.previewImageView.setImageBitmap(BitmapFactory.decodeFile(imageFile.path))
        }
    }
}

private fun processImage(): MultipartBody.Part {
    val file = reduceImageFile(selectedFile as File)
    val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
    return MultipartBody.Part.createFormData(
        "photo",
        file.name,
        requestImageFile
    )
}

override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<String>,
    grantResults: IntArray
) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    if (requestCode == REQUEST_CODE_PERMISSIONS) {
        if (!areAllPermissionsGranted()) {
            showToast("Failed to obtain permissions.")
            finish()
        }
    }
}

private fun areAllPermissionsGranted() = REQUIRED_PERMISSIONS.all {
    ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
}

private fun showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

private fun navigateToMainActivity() {
    val intent = Intent(this, MainActivity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
}
}
