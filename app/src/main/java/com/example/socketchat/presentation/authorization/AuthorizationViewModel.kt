package com.example.socketchat.presentation.authorization

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socketchat.domain.ConnectionRepository
import com.example.socketchat.utils.UNDEFINED_USERNAME
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthorizationViewModel(
    private val repository: ConnectionRepository
) : ViewModel() {

    private val _isAuthorized = MutableLiveData(false)
    val isAuthorized: LiveData<Boolean>
        get() = _isAuthorized

    private val _isConnectedToServer = MutableLiveData<Boolean>()
    val isConnectedToServer: LiveData<Boolean>
        get() = _isConnectedToServer

    private val _isUsernameError = SingleLiveEvent()
    val isUsernameError: LiveData<Boolean>
        get() = _isUsernameError

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.connectionState.collect {
                _isConnectedToServer.postValue(it)
            }
        }
        if(isAuthorized()) {
            _isAuthorized.value = true
            sendAuth()
        }
    }

    private fun sendAuth(username: String = repository.getUsername()) {
        _isAuthorized.value = true
        viewModelScope.launch(Dispatchers.IO) {
            repository.setupConnection(username)
        }
    }

    fun checkUsername(username: String) {
        if(username.isNotBlank()) {
            _isUsernameError.value = false
            sendAuth(username)
        } else {
            _isUsernameError.value = true
        }
    }

    private fun isAuthorized(): Boolean {
        return repository.getUsername() != UNDEFINED_USERNAME
    }
}