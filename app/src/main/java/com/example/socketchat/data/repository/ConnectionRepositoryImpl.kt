package com.example.socketchat.data.repository

import com.example.socketchat.data.dtomodels.BaseDto
import com.example.socketchat.data.dtomodels.ConnectDto
import com.example.socketchat.data.dtomodels.ConnectedDto
import com.example.socketchat.data.dtomodels.Payload
import com.example.socketchat.data.dtomodels.extensions.parseAction
import com.example.socketchat.domain.ConnectionRepository
import com.example.socketchat.domain.UserSharedPrefsRepository
import com.google.gson.Gson
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.Socket

class ConnectionRepositoryImpl(
    private val sharedPrefs: UserSharedPrefsRepository
) : ConnectionRepository {

    val connectionState = MutableStateFlow(false)

    private val readActionsScope = CoroutineScope(Dispatchers.IO)

    private var socketTCP: Socket? = null
    private var reader: BufferedReader? = null
    private var writer: PrintWriter? = null

    private val gson = Gson()

    private var username: String? = null

    override suspend fun setupConnection(username: String) {
        this.username = username
        if (socketTCP == null) {
            while (socketTCP == null) {
                try {
                    val address = getServerIpByUDP()
                    socketTCP = Socket(address, TCP_PORT).apply {
                        soTimeout = SOCKET_CONNECTION_TIMEOUT
                    }
                    connectionState.value = true
                    reader = BufferedReader(InputStreamReader(socketTCP?.getInputStream()))
                    writer = PrintWriter(OutputStreamWriter(socketTCP?.getOutputStream()))

                    readActionsScope.launch {
                        readActions()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private suspend fun readActions() {
        while (connectionState.value) {
            try {
                val json = reader?.readLine()
                val baseDto = gson.fromJson(json, BaseDto::class.java)

                when (val dto = baseDto.parseAction<Payload>(baseDto.action)) {
                    is ConnectedDto -> {
                        saveId(dto)
                        sendConnectDto()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun getUserId(): String {
        return sharedPrefs.getId()
    }

    private suspend fun saveId(connectedDto: ConnectedDto) {
        sharedPrefs.putId(connectedDto.id)
    }

    private suspend fun sendConnectDto() {
        val connectedUser = ConnectDto(sharedPrefs.getId(), username!!)
        val json = gson.toJson(connectedUser)
        writer?.println(json)
        writer?.flush()
    }

    private suspend fun getServerIpByUDP(): String {
        val socketUDP = DatagramSocket()
        socketUDP.soTimeout = SOCKET_CONNECTION_TIMEOUT

        val message = "message".toByteArray()

        val packet = DatagramPacket(
            message, message.size,
            InetAddress.getByName(BROADCAST_ADDRESS), UDP_PORT
        )
        var address = ""
        while (address.isEmpty()) {
            try {
                socketUDP.send(packet)
                socketUDP.receive(packet)
                address = packet.address.hostAddress
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return address
    }

    companion object {
        private const val UDP_PORT = 8888
        private const val TCP_PORT = 6666

        private const val BROADCAST_ADDRESS = "255.255.255.255"

        private const val SOCKET_CONNECTION_TIMEOUT = 2000

        private const val READ_ACTION_DELAY = 200L
    }
}