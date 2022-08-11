package com.example.socketchat.data.dtomodels.wrappers

import com.example.socketchat.data.dtomodels.MessageDto

data class MessageWrapper(
    val id: String,
    val messageDto: MessageDto
)