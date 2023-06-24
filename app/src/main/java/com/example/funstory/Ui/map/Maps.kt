package com.example.funstory.Ui.map

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.funstory.R
import com.example.funstory.data.remote.response.Story
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.funstory.databinding.ActivityMapsBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLngBounds
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class Maps : AppCompatActivity(), OnMapReadyCallback {

    private val boundsBuilder = LatLngBounds.Builder()
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val mapsViewModel: MapsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        fetchStoriesByLocation()

    }


    private fun addStoryLocationMarkers(storyList: List<Story>) {
        for (story in storyList) {
            if (story.lat != null && story.lon != null) {
                val latLng = LatLng(story.lat, story.lon)
                mMap.addMarker(
                    MarkerOptions().position(latLng).title(story.name)
                )
                if (story.id == storyList[0].id) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
                }
                boundsBuilder.include(latLng)
            }
        }
    }

    private fun fetchStoriesByLocation() {
        lifecycleScope.launch {
            mapsViewModel.getToken().collect { token ->
                if (token != null) {
                    val bearerToken = "Bearer $token"
                    mapsViewModel.getListStories(1, 10, 1, bearerToken).collectLatest { result ->
                        if (result.isSuccess) {
                            val liststoryResponse = result.getOrThrow()
                            addStoryLocationMarkers(liststoryResponse.listStory)
                        } else {
                            Toast.makeText(this@Maps, "Maps Failed: ${result.exceptionOrNull()?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}