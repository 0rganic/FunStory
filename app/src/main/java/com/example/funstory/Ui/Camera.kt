package com.example.funstory.Ui

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.example.funstory.R
import com.example.funstory.Ui.AddStory.Companion.CAMERA_X_RESULT
import com.example.funstory.databinding.ActivityCameraBinding
import com.example.funstory.utils.createPhotoFile
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Camera : AppCompatActivity() {
    private lateinit var binding: ActivityCameraBinding
    private var imageCapture: ImageCapture? = null
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.hide()

        binding.btnBack.setOnClickListener{
            val intent = Intent(this, AddStory::class.java)
            startActivity(intent)
        }

        binding.btnCapture.setOnClickListener { capturePhoto() }
        binding.btnSwitchCamera.setOnClickListener {
            cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                CameraSelector.DEFAULT_FRONT_CAMERA
            } else {
                CameraSelector.DEFAULT_BACK_CAMERA
            }
            startCamera()
        }
    }


    public override fun onResume() {
        super.onResume()
        hideSystemUI()
        startCamera()
    }

    private fun capturePhoto() {
        val currentImageCapture = imageCapture ?: return

        val photoFile = createPhotoFile(application)

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        currentImageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exception: ImageCaptureException) {
                    showToast("Failed to take picture.")
                }

                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val intent = Intent().apply {
                        putExtra("picture", photoFile)
                        putExtra("isBackCamera", cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA)
                    }
                    setResult(CAMERA_X_RESULT, intent)
                    finish()
                }
            }
        )
    }




    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.camera.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (exception: Exception) {
                showToast("Failed to load a camera.")
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun hideSystemUI() {
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

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}