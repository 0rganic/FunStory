package com.example.funstory.data.remote.repository

import com.example.funstory.data.local.UserPreferences
import com.example.funstory.data.remote.response.LoginResponse
import com.example.funstory.data.remote.response.RegistrationResponse
import com.example.funstory.data.remote.retrofit.ApiService
import com.example.funstory.utils.handleError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val apiService: ApiService,
    private val userPreferences: UserPreferences
) {

    suspend fun register(
        name: String,
        email: String,
        password: String
    ): Flow<Result<RegistrationResponse>> {
        return flow {
            val responses = apiService.register(name, email, password)
            emit(Result.success(responses))
        }.catch { throwable ->
            emit(Result.failure(Throwable(handleError(throwable))))
        }
    }

    suspend fun login(email: String, password: String): Flow<Result<LoginResponse>> {
        return flow {
            val responses = apiService.login(email, password)
            userPreferences.saveUserToken(responses.loginResult.token)
            emit(Result.success(responses))
        }.catch { throwable ->
            emit(Result.failure(Throwable(handleError(throwable))))
        }
    }

    suspend fun logout(): Flow<Result<Unit>> {
        return flow {
            userPreferences.removeUserToken()
            emit(Result.success(Unit))
        }.catch { throwable ->
            emit(Result.failure(Throwable(handleError(throwable))))
        }
    }

    fun getToken(): Flow<String?> {
        return userPreferences.getUserToken()
    }

    fun isLoggedIn(): Flow<Boolean> {
        return userPreferences.getUserToken().map { token ->
            token != null
        }
    }
}