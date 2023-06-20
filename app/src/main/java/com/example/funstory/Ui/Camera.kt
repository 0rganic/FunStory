package com.example.funstory.Ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.funstory.R
import com.example.funstory.databinding.ActivityCameraBinding

class Camera : AppCompatActivity() {
    private lateinit var binding: ActivityCameraBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.hide()
    }
}