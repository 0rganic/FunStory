package com.example.funstory.Ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.funstory.R
import com.example.funstory.databinding.ActivityAddStoryBinding

class AddStory : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title ="New Story"
    }
}