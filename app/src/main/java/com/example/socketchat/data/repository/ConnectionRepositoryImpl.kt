package com.example.socketchat.data.repository

import com.example.socketchat.data.dtomodels.*
import com.example.socketchat.data.dtomodels.extensions.parseAction
import com.example.socketchat.domain.ConnectionRepository
import com.example.socketchat.domain.UserSharedPrefsRepository
import com.example.socketchat.utils.UNDEFINED_USERNAME
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

    private var isConnected = false
    override val connectionState = MutableStateFlow(false)

    override val usersList = MutableStateFlow<List<User>>(emptyList())

    private val job = SupervisorJob()
    private val actionsScope = CoroutineScope(job + Dispatchers.IO)
    private var pingJob: Job? = null

    private var socketTCP: Socket? = null
    private var reader: BufferedReader? = null
    private var writer: PrintWriter? = null

    private val gson = Gson()

    private var id: String? = null

    override suspend fun setupConnection(username: String) {
        sharedPrefs.putUsername(username)
        while (!isConnected) {
            try {
                val address = getServerIpByUDP()
                socketTCP = Socket(address, TCP_PORT).apply {
                    soTimeout = SOCKET_CONNECTION_TIMEOUT
                }
                isConnected = true
                reader = BufferedReader(InputStreamReader(socketTCP?.getInputStream()))
                writer = PrintWriter(OutputStreamWriter(socketTCP?.getOutputStream()))

                actionsScope.launch {
                    readActions()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun readActions() {
        while (isConnected) {
            try {
                val json = reader?.readLine()
                val baseDto = gson.fromJson(json, BaseDto::class.java)

                when (val dto = baseDto.parseAction<Payload>(baseDto.action)) {
                    is ConnectedDto -> {
                        onConnectedToServer(dto)
                    }
                    is PongDto -> {
                        pingJob?.cancel()
                    }
                    is UsersReceivedDto -> {
                        parseUsersReceivedDto(dto)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override suspend fun getUsersList() {
        id?.also {
            val getUserList = GetUsersDto(it)
            val json = gson.toJson(getUserList)
            val baseDto = BaseDto(BaseDto.Action.GET_USERS, json)
            sendBaseDtoToServer(baseDto)
        }
    }

    private fun parseUsersReceivedDto(usersReceivedDto: UsersReceivedDto) {
        usersList.value = usersReceivedDto.users
    }

    private suspend fun onConnectedToServer(dto: ConnectedDto) {
        id = dto.id
        sendConnectDto()
        connectionState.value = true
        actionsScope.launch {
            while (connectionState.value) {
                id?.also {
                    val pingDto = PingDto(it)
                    val json = gson.toJson(pingDto)
                    val baseDto = BaseDto(BaseDto.Action.PING, json)
                    sendBaseDtoToServer(baseDto)

                    pingJob = getPingJob()
                    delay(PING_PONG_TIMEOUT)
                }
            }
        }
    }

    override fun logOut() {
        sharedPrefs.putUsername(UNDEFINED_USERNAME)
    }

    private suspend fun sendConnectDto() {
        id?.also {
            val connectedUser = ConnectDto(it, sharedPrefs.getUsername())
            val json = gson.toJson(connectedUser)
            val baseDto = BaseDto(BaseDto.Action.CONNECT, json)
            sendBaseDtoToServer(baseDto)
        }
    }

    private fun getPingJob(): Job = actionsScope.launch {
        delay(PING_PONG_TIMEOUT)

        closeConnection()
    }

    private suspend fun sendBaseDtoToServer(baseDto: BaseDto) {
        val json = gson.toJson(baseDto)
        writer?.println(json)
        writer?.flush()
    }

    private suspend fun closeConnection() {
        reader?.close()
        writer?.close()
        socketTCP?.close()
        connectionState.value = false
        job.cancelChildren()
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

    override fun getConnectionState(): Boolean {
        return connectionState.value
    }

    companion object {
        private const val UDP_PORT = 8888
        private const val TCP_PORT = 6666

//        private const val BROADCAST_ADDRESS = "10.0.2.2"
        private const val BROADCAST_ADDRESS = "255.255.255.255"

        private const val SOCKET_CONNECTION_TIMEOUT = 2000

        private const val PING_PONG_TIMEOUT = 10000L
    }
}