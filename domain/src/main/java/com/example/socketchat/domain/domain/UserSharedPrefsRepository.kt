package com.example.socketchat.domain.domain

internal interface UserSharedPrefsRepository {

    fun putUsername(username: String)

    fun getUsername(): String
}