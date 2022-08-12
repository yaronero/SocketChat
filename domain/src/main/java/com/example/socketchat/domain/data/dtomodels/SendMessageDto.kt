package com.example.socketchat.domain.data.dtomodels

data class SendMessageDto(val id: String, val receiver: String, val message: String) : Payload