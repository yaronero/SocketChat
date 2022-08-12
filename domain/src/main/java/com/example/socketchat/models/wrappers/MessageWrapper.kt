package com.example.socketchat.models.wrappers

import com.example.socketchat.models.dtomodels.MessageDto

data class MessageWrapper(
    val id: String,
    val messageDto: MessageDto
)