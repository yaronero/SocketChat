package com.example.socketchat.domain

interface UserSharedPrefsRepository {

    fun putUsername(username: String)

    fun getUsername(): String

    fun isUserAuthorized(): Boolean
}