package com.example.socketchat.models.dtomodels

data class SendMessageDto(val id: String, val receiver: String, val message: String) : Payload