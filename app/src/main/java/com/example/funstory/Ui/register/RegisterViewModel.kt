package com.example.funstory.Ui.register

import androidx.lifecycle.ViewModel
import com.example.funstory.data.remote.repository.AuthRepository
import com.example.funstory.data.remote.response.RegistrationResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel(){

    suspend fun register(
        name: String,
        email: String,
        password: String
    ): kotlinx.coroutines.flow.Flow<Result<RegistrationResponse>> {
        return authRepository.register(name, email, password)
    }
}