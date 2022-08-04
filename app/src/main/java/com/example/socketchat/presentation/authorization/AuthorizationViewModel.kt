package com.example.socketchat.presentation.authorization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socketchat.domain.ConnectRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthorizationViewModel(
    private val repository: ConnectRepository
) : ViewModel() {

    fun connectToServer() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.connectToServer()
        }
    }
}