package com.example.socketchat.presentation.authorization

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socketchat.domain.ConnectionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthorizationViewModel(
    private val repository: ConnectionRepository
) : ViewModel() {

    private val _isConnectedToServer = MutableLiveData<Boolean>()
    val isConnectedToServer: LiveData<Boolean>
        get() = _isConnectedToServer

    private val _username = MutableLiveData<String>()

    private val _usernameError = SingleLiveEvent()
    val usernameError: LiveData<Boolean>
        get() = _usernameError

    fun sendAuth() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.setupConnection(_username.value!!)
        }
        viewModelScope.launch(Dispatchers.IO) {
            repository.connectionState.collect {
                _isConnectedToServer.postValue(it)
            }
        }
    }

    fun checkUsername(username: String) {
        _username.value = username
        _usernameError.value = username.isBlank()
    }
}