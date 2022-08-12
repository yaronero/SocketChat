package com.example.socketchat.domain

import com.example.socketchat.models.dtomodels.User
import com.example.socketchat.models.wrappers.MessageWrapper
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface ConnectionRepository {

    val connectionState: StateFlow<Boolean>

    val usersList: StateFlow<List<User>>

    val newMessage: SharedFlow<MessageWrapper>

    suspend fun setupConnection(username: String)

    suspend fun getUsersList()

    suspend fun sendMessage(receiverId: String, message: String)

    fun getId(): String?

    fun getUsername(): String

    suspend fun logOut()

    fun getConnectionState(): Boolean
}