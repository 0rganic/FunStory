package com.example.funstory.Ui.main

import androidx.lifecycle.ViewModel
import com.example.funstory.data.remote.repository.AuthRepository
import com.example.funstory.data.remote.repository.StoryRepository
import com.example.funstory.data.remote.response.StoryResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val storyRepository: StoryRepository, private val authRepository: AuthRepository): ViewModel() {
    fun getStories(
        page: Int?,
        size: Int?,
        location: Int = 0,
        authHeader: String
    ): Flow<Result<StoryResponse>> {
        return storyRepository.getListStories(page, size, location, authHeader)
    }

    fun getToken(): Flow<String?> {
        return authRepository.getToken()
    }

    suspend fun logOut(): Flow<Result<Unit>> {
        return authRepository.logout()
    }
}