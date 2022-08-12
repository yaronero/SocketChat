package com.example.socketchat.domain.data.dtomodels.wrappers

import com.example.socketchat.domain.data.dtomodels.MessageDto

data class MessageWrapper(
    val id: String,
    val messageDto: MessageDto
)