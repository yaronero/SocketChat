package com.example.socketchat.domain

interface ConnectRepository {

    suspend fun connectToServer()
}