package com.example.socketchat.data.repository

import com.example.socketchat.data.dtomodels.*
import com.example.socketchat.data.dtomodels.extensions.parseAction
import com.example.socketchat.domain.ConnectionRepository
import com.example.socketchat.domain.UserSharedPrefsRepository
import com.google.gson.Gson
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.*
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.Socket

class ConnectionRepositoryImpl(
    private val sharedPrefs: UserSharedPrefsRepository
) : ConnectionRepository {

    override val connectionState = MutableStateFlow(false)
    override val isUserAuthorized = MutableStateFlow(false)

    override val usersList = MutableStateFlow<List<User>>(emptyList())

    private val actionsScope = CoroutineScope(Dispatchers.IO)
    private var pingJob: Job? = null

    private var socketTCP: Socket? = null
    private var reader: BufferedReader? = null
    private var writer: PrintWriter? = null

    private val gson = Gson()

    private var username: String? = null

    override suspend fun setupConnection(username: String) {
        this.username = username
        while (!connectionState.value) {
            try {
                val address = getServerIpByUDP()
                socketTCP = Socket(address, TCP_PORT).apply {
                    soTimeout = SOCKET_CONNECTION_TIMEOUT
                }
                connectionState.value = true
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
        while (connectionState.value) {
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
        val getUserList = GetUsersDto(sharedPrefs.getId())
        val json = gson.toJson(getUserList)
        val baseDto = BaseDto(BaseDto.Action.GET_USERS, json)
        sendBaseDtoToServer(baseDto)
    }

    private fun parseUsersReceivedDto(usersReceivedDto: UsersReceivedDto) {
        usersList.value = usersReceivedDto.users
    }

    override fun getUserId(): String {
        return sharedPrefs.getId()
    }

    private suspend fun saveId(connectedDto: ConnectedDto) {
        sharedPrefs.putId(connectedDto.id)
    }

    private suspend fun onConnectedToServer(dto: ConnectedDto) {
        saveId(dto)
        sendConnectDto()
        setUserAuthorized(true)
        actionsScope.launch {
            while (connectionState.value) {
                val pingDto = PingDto(sharedPrefs.getId())
                val json = gson.toJson(pingDto)
                val baseDto = BaseDto(BaseDto.Action.PING, json)
                sendBaseDtoToServer(baseDto)

                pingJob = getPingJob()
                delay(PING_PONG_TIMEOUT)
            }
        }
    }

    override fun isUserAuthorized(): Boolean {
        return sharedPrefs.isUserAuthorized()
    }

    override fun setUserAuthorized(isUserAuthorized: Boolean) {
        sharedPrefs.setIfUserAuthorized(isUserAuthorized)
        this.isUserAuthorized.value = true
    }

    private suspend fun sendConnectDto() {
        val connectedUser = ConnectDto(sharedPrefs.getId(), username!!)
        val json = gson.toJson(connectedUser)
        val baseDto = BaseDto(BaseDto.Action.CONNECT, json)
        sendBaseDtoToServer(baseDto)
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
        actionsScope.cancel()
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

        private const val BROADCAST_ADDRESS = "10.0.2.2"
//        private const val BROADCAST_ADDRESS = "255.255.255.255"

        private const val SOCKET_CONNECTION_TIMEOUT = 2000

        private const val PING_PONG_TIMEOUT = 10000L
    }
}