package com.example.funstory.Ui.addStory

import androidx.lifecycle.ViewModel
import com.example.funstory.data.remote.repository.AuthRepository
import com.example.funstory.data.remote.repository.StoryRepository
import com.example.funstory.data.remote.response.AddStory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class AddStoryViewModel @Inject constructor(
    private val storyRepository: StoryRepository,
    private val authRepository: AuthRepository
): ViewModel() {
    fun getToken(): Flow<String?> {
        return authRepository.getToken()
    }

    suspend fun addStory(
        description: String,
        photo: MultipartBody.Part,
        lat: Double?,
        lon: Double?,
        authHeader: String
    ): Flow<Result<AddStory>> {
        return storyRepository.addStory(description, photo, lat, lon, authHeader)
    }


}