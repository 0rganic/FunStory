package com.example.funstory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class Camera : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        supportActionBar!!.hide()
    }
}