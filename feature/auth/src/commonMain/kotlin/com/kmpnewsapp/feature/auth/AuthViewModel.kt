package com.kmpnewsapp.feature.auth

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.kmpnewsapp.core.database.NewsDao
import com.kmpnewsapp.core.database.UserEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

class AuthViewModel(private val newsDao: NewsDao) : ScreenModel {

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state.asStateFlow()

    fun signUp(fullName: String, email: String, password: String) {
        if (fullName.isBlank() || email.isBlank() || password.isBlank()) {
            _state.update { it.copy(error = "Please fill all fields") }
            return
        }

        screenModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val existingUser = newsDao.getUserByEmail(email)
                if (existingUser != null) {
                    _state.update { it.copy(isLoading = false, error = "User already exists") }
                } else {
                    val newUser = UserEntity(email, fullName, password)
                    newsDao.insertUser(newUser)
                    _state.update { it.copy(isLoading = false, isSuccess = true) }
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _state.update { it.copy(error = "Please fill all fields") }
            return
        }

        screenModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val user = newsDao.getUserByEmail(email)
                if (user != null && user.password == password) {
                    _state.update { it.copy(isLoading = false, isSuccess = true) }
                } else {
                    _state.update { it.copy(isLoading = false, error = "Invalid email or password") }
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}
