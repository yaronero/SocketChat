package com.example.socketchat.domain

interface ConnectionRepository {

    suspend fun setupConnection()

    suspend fun sendAuth(id: String, username: String)

    suspend fun getId(): String
}