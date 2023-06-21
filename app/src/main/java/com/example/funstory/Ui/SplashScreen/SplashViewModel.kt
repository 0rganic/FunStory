package com.example.funstory.Ui.SplashScreen

import androidx.lifecycle.ViewModel
import com.example.funstory.data.remote.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel(){
    fun isUserLoggedIn(): Flow<Boolean> {
        return authRepository.isLoggedIn()
    }
}