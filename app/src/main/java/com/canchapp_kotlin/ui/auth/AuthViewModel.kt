package com.canchapp_kotlin.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canchapp_kotlin.data.network.LoginResponse
import com.canchapp_kotlin.data.network.RegisterResponse
import com.canchapp_kotlin.data.network.UserDto
import com.canchapp_kotlin.data.repository.AuthRepository
import com.canchapp_kotlin.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val repository = AuthRepository()

    private val _loginState = MutableStateFlow<Resource<LoginResponse>?>(null)
    val loginState = _loginState.asStateFlow()

    private val _registerState = MutableStateFlow<Resource<RegisterResponse>?>(null)
    val registerState = _registerState.asStateFlow()

    private val _loggedUser = MutableStateFlow<UserDto?>(null)
    val loggedUser = _loggedUser.asStateFlow()

    fun login(identifier: String, password: String) {
        viewModelScope.launch {
            _loginState.value = Resource.Loading
            val result = repository.login(identifier, password)
            _loginState.value = result
            if (result is Resource.Success) {
                _loggedUser.value = result.data.data?.user
            }
        }
    }

    fun register(
        email: String,
        firstName: String,
        lastName: String,
        password: String,
        roleName: String,
        username: String
    ) {
        viewModelScope.launch {
            _registerState.value = Resource.Loading
            _registerState.value = repository.register(email, firstName, lastName, password, roleName, username)
        }
    }

    fun logout() {
        _loggedUser.value = null
        _loginState.value = null
    }

    fun resetLoginState() { _loginState.value = null }
    fun resetRegisterState() { _registerState.value = null }
}
