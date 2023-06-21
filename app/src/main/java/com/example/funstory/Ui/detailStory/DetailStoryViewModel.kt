package com.example.funstory.Ui.detailStory

import androidx.lifecycle.ViewModel
import com.example.funstory.data.remote.repository.AuthRepository
import com.example.funstory.data.remote.repository.StoryRepository
import com.example.funstory.data.remote.response.DetailStory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


@HiltViewModel
class DetailStoryViewModel @Inject constructor (
    private val storyRepository: StoryRepository,
    private val authRepository: AuthRepository
    ) : ViewModel()   {
    fun getToken(): Flow<String?> {
        return authRepository.getToken()
    }
    fun getDetailStory(
        id: String,
        authHeader: String
    ): Flow<Result<DetailStory>> {
        return storyRepository.getStoryDetail(id, authHeader)
    }


}