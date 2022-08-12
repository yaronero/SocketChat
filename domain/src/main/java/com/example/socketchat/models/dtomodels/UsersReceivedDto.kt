package com.example.socketchat.models.dtomodels

data class UsersReceivedDto(val users: List<User>) : Payload

data class User(val id: String, val name: String)