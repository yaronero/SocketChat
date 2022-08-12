package com.example.socketchat.presentation.userslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socketchat.domain.data.dtomodels.User
import com.example.socketchat.domain.domain.ConnectionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class UsersListViewModel(
    private val repository: ConnectionRepository
) : ViewModel() {

    private val _usersList = MutableLiveData<List<User>>()
    val usersList: LiveData<List<User>>
        get() = _usersList

    fun getAllUsers(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.connectionState.collect {
                while(repository.getConnectionState()) {
                    delay(300)
                    repository.getUsersList()
                }
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            repository.usersList.collect {
                _usersList.postValue(it)
            }
        }
    }

    fun logOut() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.logOut()
        }
    }
}