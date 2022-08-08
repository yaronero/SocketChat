package com.example.socketchat.domain

interface UserSharedPrefsRepository {

    fun putId(id: String)

    fun getId(): String

    fun setIfUserAuthorized(isUserAuthorized: Boolean)

    fun isUserAuthorized(): Boolean
}