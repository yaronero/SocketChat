package com.example.socketchat.presentation.authorization

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socketchat.domain.ConnectionRepository
import com.example.socketchat.utils.UNDEFINED_ID
import kotlinx.coroutines.*

class AuthorizationViewModel(
    private val repository: ConnectionRepository
) : ViewModel() {

    private val _userId = MutableLiveData<String>()
    val userId: LiveData<String>
        get() = _userId

    private val _username = MutableLiveData<String>()

    private val _usernameError = MutableLiveData<Boolean>()
    val usernameError: LiveData<Boolean>
        get() = _usernameError

    fun sendAuth() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.setupConnection(_username.value!!)
        }
        viewModelScope.launch(Dispatchers.IO) {
            _userId.postValue(loadUserId())
        }
    }

    fun checkUsername(username: String) {
        _username.value = username
        _usernameError.value = username.isBlank()
    }

    private suspend fun loadUserId(): String {
        var userId = UNDEFINED_ID
        while (userId == UNDEFINED_ID) {
            delay(GET_ID_OPERATION_DELAY)
            userId = repository.getUserId()
        }
        return userId
    }

    companion object {
        private const val GET_ID_OPERATION_DELAY = 200L
    }
}