package com.example.socketchat.domain

interface ConnectionRepository {

    suspend fun setupConnection(username: String)

    fun getUserId(): String
}