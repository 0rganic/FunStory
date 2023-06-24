package com.example.funstory.Ui.map

import androidx.lifecycle.ViewModel
import com.example.funstory.data.remote.repository.AuthRepository
import com.example.funstory.data.remote.repository.StoryRepository
import com.example.funstory.data.remote.response.StoryResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor (private val authRepository: AuthRepository, private val storyRepository: StoryRepository): ViewModel(){

    fun getToken(): Flow<String?> {
        return authRepository.getToken()
    }

    fun getListStories(
        page: Int?,
        size: Int?,
        location:Int = 0,
        authHeader:String
    ): Flow<Result<StoryResponse>> {
        return storyRepository.getListStoriesWithoutPaging(page, size, location, authHeader)
    }

}