package com.example.socketchat.data.dtomodels.extensions

import com.example.socketchat.data.dtomodels.*
import com.google.gson.Gson

fun <T: Payload> BaseDto.parseAction(action: BaseDto.Action): T {
    val gson = Gson()
    return when(action) {
        BaseDto.Action.CONNECT -> gson.fromJson(payload, ConnectDto::class.java)
        BaseDto.Action.CONNECTED -> gson.fromJson(payload, ConnectedDto::class.java)
        BaseDto.Action.PING -> TODO()
        BaseDto.Action.PONG -> gson.fromJson(payload, PongDto::class.java)
        BaseDto.Action.GET_USERS -> TODO()
        BaseDto.Action.USERS_RECEIVED -> gson.fromJson(payload, UsersReceivedDto::class.java)
        BaseDto.Action.SEND_MESSAGE -> TODO()
        BaseDto.Action.NEW_MESSAGE -> gson.fromJson(payload, MessageDto::class.java)
        BaseDto.Action.DISCONNECT -> TODO()
    } as T
}