package com.example.funstory

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.funstory.data.remote.response.Story

class TestStoryPagingSourceUtils: PagingSource<Int, LiveData<List<Story>>>() {
    companion object {
        fun createSnapshot(items: List<Story>): PagingData<Story> {
            return PagingData.from(items)
        }
    }


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<Story>>> {
        return LoadResult.Page(emptyList(), prevKey = 0, nextKey = 1)
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<Story>>>): Int {
        return 0
    }
}