package com.example.socketchat.data.extensions

import com.example.socketchat.models.dtomodels.*
import com.google.gson.Gson

fun <T: Payload> BaseDto.parseAction(action: BaseDto.Action): T {
    val gson = Gson()
    return when(action) {
        BaseDto.Action.CONNECT -> gson.fromJson(payload, ConnectDto::class.java)
        BaseDto.Action.CONNECTED -> gson.fromJson(payload, com.example.socketchat.models.dtomodels.ConnectedDto::class.java)
        BaseDto.Action.PING -> TODO()
        BaseDto.Action.PONG -> gson.fromJson(payload, com.example.socketchat.models.dtomodels.PongDto::class.java)
        BaseDto.Action.GET_USERS -> TODO()
        BaseDto.Action.USERS_RECEIVED -> gson.fromJson(payload, UsersReceivedDto::class.java)
        BaseDto.Action.SEND_MESSAGE -> TODO()
        BaseDto.Action.NEW_MESSAGE -> gson.fromJson(payload, com.example.socketchat.models.dtomodels.MessageDto::class.java)
        BaseDto.Action.DISCONNECT -> TODO()
    } as T
}