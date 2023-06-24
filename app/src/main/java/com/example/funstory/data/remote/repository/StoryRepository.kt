package com.example.funstory.data.remote.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.funstory.data.remote.StoryPaggingSource
import com.example.funstory.data.remote.response.AddStory
import com.example.funstory.data.remote.response.DetailStory
import com.example.funstory.data.remote.response.Story
import com.example.funstory.data.remote.response.StoryResponse
import com.example.funstory.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import javax.inject.Inject

class StoryRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun addStory(
        description: String,
        photo: MultipartBody.Part,
        lat: Double?,
        lon: Double?,
        authHeader: String
    ): Flow<Result<AddStory>> {
        return flow {
            val response = apiService.addStory(description, photo, lat, lon, authHeader)
            emit(Result.success(response))
        }.catch { throwable ->
            emit(Result.failure(throwable))
        }
    }

    fun getListStories(
        authHeader: String
    ): LiveData<PagingData<Story>>{
        return Pager (
            config = PagingConfig(10),
        pagingSourceFactory ={
            StoryPaggingSource(apiService, authHeader)
        }
        ).liveData
    }

    fun getStoryDetail(
        id: String,
        authHeader: String
    ): Flow<Result<DetailStory>> {
        return flow {
            val response = apiService.getStoryDetail(id, authHeader)
            emit(Result.success(response))
        }.catch { throwable ->
            emit(Result.failure(throwable))
        }
    }

    fun getListStoriesWithoutPaging(
        page: Int?,
        size: Int?,
        location: Int = 0,
        authHeader: String
    ): Flow<Result<StoryResponse>> {
        return flow {
            val response = apiService.getListStories(page, size, location, authHeader)
            emit(Result.success(response))
        }.catch { throwable ->
            emit(Result.failure(throwable))
        }
    }
}
