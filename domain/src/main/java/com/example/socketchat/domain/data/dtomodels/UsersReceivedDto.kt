package com.example.socketchat.domain.data.dtomodels

data class UsersReceivedDto(val users: List<User>) : Payload

data class User(val id: String, val name: String)