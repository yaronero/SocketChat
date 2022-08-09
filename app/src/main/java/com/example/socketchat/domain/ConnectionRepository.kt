package com.example.socketchat.domain

import com.example.socketchat.data.dtomodels.User
import kotlinx.coroutines.flow.MutableStateFlow

interface ConnectionRepository {

    val connectionState: MutableStateFlow<Boolean>

    val usersList: MutableStateFlow<List<User>>

    suspend fun setupConnection(username: String)

    suspend fun getUsersList()

    fun logOut()

    fun isUserAuthorized(): Boolean
}