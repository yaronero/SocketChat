package com.example.socketchat.models.dtomodels

data class MessageDto(val from: User, val message: String) : Payload