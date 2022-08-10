package com.example.socketchat.domain

import com.example.socketchat.data.dtomodels.User
import kotlinx.coroutines.flow.StateFlow

interface ConnectionRepository {

    val connectionState: StateFlow<Boolean>

    val usersList: StateFlow<List<User>>

    suspend fun setupConnection(username: String)

    suspend fun getUsersList()

    fun logOut()

    fun getConnectionState(): Boolean
}