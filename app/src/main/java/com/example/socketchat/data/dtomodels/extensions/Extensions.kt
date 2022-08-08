package com.example.socketchat.data.dtomodels.extensions

import com.example.socketchat.data.dtomodels.BaseDto
import com.example.socketchat.data.dtomodels.ConnectDto
import com.example.socketchat.data.dtomodels.ConnectedDto
import com.example.socketchat.data.dtomodels.Payload
import com.google.gson.Gson

fun <T: Payload> BaseDto.parseAction(action: BaseDto.Action): T {
    val gson = Gson()
    return when(action) {
        BaseDto.Action.CONNECT -> gson.fromJson(payload, ConnectDto::class.java)
        BaseDto.Action.CONNECTED -> gson.fromJson(payload, ConnectedDto::class.java)
        BaseDto.Action.PING -> TODO()
        BaseDto.Action.PONG -> TODO()
        BaseDto.Action.GET_USERS -> TODO()
        BaseDto.Action.USERS_RECEIVED -> TODO()
        BaseDto.Action.SEND_MESSAGE -> TODO()
        BaseDto.Action.NEW_MESSAGE -> TODO()
        BaseDto.Action.DISCONNECT -> TODO()
    } as T
}