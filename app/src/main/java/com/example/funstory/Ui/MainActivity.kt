package com.example.funstory.Ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.funstory.R
import com.example.funstory.Ui.adapter.StoryAdapter
import com.example.funstory.Ui.viewModel.MainViewModel
import com.example.funstory.data.remote.response.Story
import com.example.funstory.databinding.ActivityMainBinding
import com.example.funstory.utils.SpaceItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fetchData()

        binding.btnAddStory.setOnClickListener {
            val intent = Intent(this, AddStory::class.java)
            startActivity(intent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.btn_signout -> {
                logOut()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun logOut() {
        lifecycleScope.launch {
            mainViewModel.logOut().collect(){ result ->
                if (result.isSuccess){
                    Toast.makeText(this@MainActivity, "logout Success", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@MainActivity, Login::class.java)
                    startActivity(intent)
                }else {
                    Toast.makeText(this@MainActivity, "Logout Failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun fetchData() {
        lifecycleScope.launch {
            mainViewModel.getToken().collect { token ->
                if (token != null) {
                    val jwt = "Bearer $token"
                    showLoading()
                    mainViewModel.getStories(1, 10, 0, jwt).collectLatest { result ->
                        hideLoading()
                        if (result.isSuccess) {
                            val response = result.getOrThrow()
                            setStory(response.listStory)
                        } else {
                            Toast.makeText(this@MainActivity, "Failed: ${result.exceptionOrNull()}", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    val intent = Intent(this@MainActivity, Login::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun setStory(storyList: List<Story>?) {
        val layoutManager = LinearLayoutManager(this)
        binding.rvListStory.layoutManager = layoutManager

        val adapter = StoryAdapter(storyList ?: emptyList()) { story ->
            val intent = Intent(this@MainActivity, DetailStory::class.java)
            intent.putExtra(DetailStory.ID, story.id)
            startActivity(intent)
        }
        val spaceInPixels = resources.getDimensionPixelSize(R.dimen.item_spacing)
        binding.rvListStory.addItemDecoration(SpaceItemDecoration(spaceInPixels))
        binding.rvListStory.adapter = adapter
    }

    private fun hideLoading() {
        binding.loading.visibility = View.INVISIBLE
    }

    private fun showLoading() {
        binding.loading.visibility = View.VISIBLE
    }
}