package com.example.funstory

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.funstory.Ui.adapter.StoryAdapter
import com.example.funstory.Ui.main.MainViewModel
import com.example.funstory.data.remote.repository.AuthRepository
import com.example.funstory.data.remote.repository.StoryRepository
import com.example.funstory.data.remote.response.Story
import junit.framework.TestCase.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@Suppress("DEPRECATION")
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcher = MainDispatcher()

    @Mock
    private lateinit var storyRepository: StoryRepository

    @Mock
    private lateinit var authRepository: AuthRepository

    @Test
    fun `getStories() should return data`() = runTest {
        // Dummy data
        val dummyStories = listOf(
            Story("1", "Jeremy 1", "lalala", "https://unsplash.com/photos/Mx-u0nHMxjs", "2022-05-03", 4.5, 20.0 ),
            Story("2", "Jeremy 2", "bababa", "https://unsplash.com/photos/4hbJ-eymZ1o", "2023-06-09", 4.5, 20.0 ),
            Story("3", "Jeremy 3", "cacaca", "https://unsplash.com/photos/m_HRfLhgABo", "2021-10-01", 4.5, 20.0 )
        )

        val data: PagingData<Story> = TestStoryPagingSourceUtils.createSnapshot(dummyStories)
        val expectedPagingData = MutableLiveData<PagingData<Story>>()
        expectedPagingData.value = data

        // Mock repository response
        `when`(storyRepository.getListStories(anyString())).thenReturn(expectedPagingData)

        // Create ViewModel
        val viewModel = MainViewModel(storyRepository, authRepository)

        // Invoke the function to get stories
        val result: PagingData<Story> = viewModel.getStories("dummyToken").getOrAwaitValueCustom()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )

        differ.submitData(result)

        // Verify the result
        assertNotNull(differ.snapshot())
        assertEquals(dummyStories[0], differ.snapshot()[0])
        assertEquals(dummyStories.size, differ.snapshot().size)
    }

    @Test
    fun `getZeroStory() should return 0 data`() = runTest {

        val data: PagingData<Story> = TestStoryPagingSourceUtils.createSnapshot(emptyList())
        val expectedPagingData = MutableLiveData<PagingData<Story>>()
        expectedPagingData.value = data

        // Mock repository response
        `when`(storyRepository.getListStories(anyString())).thenReturn(expectedPagingData)

        // Create ViewModel
        val viewModel = MainViewModel(storyRepository, authRepository)

        // Invoke the function to get stories
        val result: PagingData<Story> = viewModel.getStories("dummyToken").getOrAwaitValueCustom()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )

        differ.submitData(result)

        // Verify the result
        assertNotNull(differ.snapshot())
        assertEquals(0, differ.snapshot().size)
    }

    @Test
    fun `getToken() should return token flow`() = runBlockingTest {
        // Dummy token
        val dummyToken = "dummyToken"

        // Mock repository response
        `when`(authRepository.getToken()).thenReturn(flowOf(dummyToken))

        // Create ViewModel
        val viewModel = MainViewModel(storyRepository, authRepository)

        // Invoke the function to get token
        val result = viewModel.getToken().first()

        // Verify the result
        assertEquals(dummyToken, result)
    }

    @Test
    fun `logOut() should return success result`() = runBlockingTest {
        // Mock repository response
        `when`(authRepository.logout()).thenReturn(flowOf(Result.success(Unit)))

        // Create ViewModel
        val viewModel = MainViewModel(storyRepository, authRepository)

        // Invoke the function to log out
        val result = viewModel.logOut().first()

        // Verify the result
        assertTrue(result.isSuccess)
        assertNull(result.exceptionOrNull())
    }
    companion object {
        val noopListUpdateCallback = object : ListUpdateCallback {
            override fun onInserted(position: Int, count: Int) {}
            override fun onRemoved(position: Int, count: Int) {}
            override fun onMoved(fromPosition: Int, toPosition: Int) {}
            override fun onChanged(position: Int, count: Int, payload: Any?) {}
        }
    }
}

