package com.example.funstory.Ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.funstory.R
import com.example.funstory.Ui.adapter.LoadingPagingAdapter
import com.example.funstory.Ui.adapter.StoryAdapter
import com.example.funstory.Ui.addStory.AddStory
import com.example.funstory.Ui.detailStory.DetailStory
import com.example.funstory.Ui.login.Login
import com.example.funstory.Ui.map.Maps
import com.example.funstory.databinding.ActivityMainBinding
import com.example.funstory.utils.SpaceItemDecoration
import dagger.hilt.android.AndroidEntryPoint
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
            R.id.btn_location -> {
                val intent = Intent(this, Maps::class.java)
                startActivity(intent)
                true
            } else -> super.onOptionsItemSelected(item)
        }
    }

    private fun logOut() {
        lifecycleScope.launch {
            mainViewModel.logOut().collect { result ->
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
                    setStory(jwt)
                }
            }
        }
    }

    private fun setStory(token: String) {
        val layoutManager = LinearLayoutManager(this)
        binding.rvListStory.layoutManager = layoutManager

        val adapter = StoryAdapter() { story ->
            val intent = Intent(this@MainActivity, DetailStory::class.java)
            intent.putExtra(DetailStory.ID, story.id)
            startActivity(intent)
        }
        binding.rvListStory.adapter = adapter.withLoadStateFooter (footer = LoadingPagingAdapter{
            adapter.retry()
        })
        mainViewModel.getStories(token).observe(this) {
            adapter.submitData(lifecycle, it)
        }
        val spaceInPixels = resources.getDimensionPixelSize(R.dimen.item_spacing)
        binding.rvListStory.addItemDecoration(SpaceItemDecoration(spaceInPixels))
        binding.rvListStory.adapter = adapter
    }


}