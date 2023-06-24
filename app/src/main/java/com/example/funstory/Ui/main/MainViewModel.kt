package com.example.funstory.Ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.funstory.data.remote.repository.AuthRepository
import com.example.funstory.data.remote.repository.StoryRepository
import com.example.funstory.data.remote.response.Story
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val storyRepository: StoryRepository, private val authRepository: AuthRepository): ViewModel() {
    fun getStories(
        authHeader: String
    ): LiveData<PagingData<Story>> {
        return storyRepository.getListStories(authHeader).cachedIn(viewModelScope)
    }

    fun getToken(): Flow<String?> {
        return authRepository.getToken()
    }

    suspend fun logOut(): Flow<Result<Unit>> {
        return authRepository.logout()
    }
}