package com.example.funstory.Ui.detailStory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.funstory.data.remote.response.Story
import com.example.funstory.databinding.ActivityDetailStoryBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailStory : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding
    private val detailViewModel: DetailStoryViewModel by viewModels()
    companion object {
        const val ID = "id_story"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val idStory = intent.getStringExtra(ID)

        setDetailStory(idStory)

    }

    private fun setDetailStory(idStory: String?) {
        lifecycleScope.launch {
            detailViewModel.getToken().collect{token ->
                if(token != null && idStory != null) {
                    val jwt = "Bearer $token"
                    binding.loading.visibility = View.VISIBLE
                    detailViewModel.getDetailStory(idStory, jwt).collect{result ->
                        binding.loading.visibility = View.INVISIBLE
                        if (result.isSuccess){
                            val storyDetail = result.getOrThrow()
                            displayStoryDetail(storyDetail.story)
                        }else{
                            val errorMesasge = result.exceptionOrNull()?.message
                            Toast.makeText(this@DetailStory, "Detail Failed: $errorMesasge", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

    }

    private fun displayStoryDetail(story: Story) {
        binding.tvDetailName.text = story.name
        binding.tvDetailDescription.text = story.description
        Glide.with(this)
            .load(story.photoUrl)
            .into(binding.ivDetailPhoto)

    }
}