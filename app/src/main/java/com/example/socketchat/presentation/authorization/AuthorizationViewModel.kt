package com.example.socketchat.presentation.authorization

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socketchat.domain.ConnectionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthorizationViewModel(
    private val repository: ConnectionRepository
) : ViewModel() {

    private val _id = MutableLiveData<String>()

    private val _username = MutableLiveData<String>()
    val username: LiveData<String>
        get() = _username

    private val _usernameError = MutableLiveData<Boolean>()
    val usernameError: LiveData<Boolean>
        get() = _usernameError

    fun connectToServer() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.setupConnection()
            withContext(Dispatchers.Main) {
                _id.value = repository.getId()
            }
        }
    }

    fun sendAuth() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.sendAuth(_id.value!!, _username.value!!)
        }
    }

    fun checkUsername(username: String) {
        _username.value = username
        _usernameError.value = username.isBlank()
    }
}