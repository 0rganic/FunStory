package com.example.funstory.Ui.viewModel

import androidx.lifecycle.ViewModel
import com.example.funstory.data.remote.repository.AuthRepository
import com.example.funstory.data.remote.response.LoginResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val authRepository: AuthRepository): ViewModel() {

    suspend fun login(
        email: String,
        password: String
    ): Flow<Result<LoginResponse>> {
        return authRepository.login(email, password)
    }

}