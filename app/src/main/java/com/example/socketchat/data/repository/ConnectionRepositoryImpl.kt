package com.example.socketchat.data.repository

import com.example.socketchat.data.PayloadFactory
import com.example.socketchat.data.dtomodels.BaseDto
import com.example.socketchat.data.dtomodels.ConnectDto
import com.example.socketchat.data.dtomodels.ConnectedDto
import com.example.socketchat.domain.ConnectionRepository
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.PrintWriter
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.Socket

class ConnectionRepositoryImpl : ConnectionRepository {

    private var socketTCP: Socket? = null
    private var reader: BufferedReader? = null
    private var writer: PrintWriter? = null

    private val gson = Gson()

    override suspend fun setupConnection() {
        if (socketTCP == null) {
            while (socketTCP == null) {
                try {
                    val address = getServerIpByUDP()
                    socketTCP = Socket(address, TCP_PORT)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    override suspend fun sendAuth(id: String, username: String) {
        val connectDto = ConnectDto(id, username)
        val json = gson.toJson(connectDto)
        writer?.println(json)
        writer?.flush()
    }

    override suspend fun getId(): String {
        val json = reader?.readLine()
        val baseDto = gson.fromJson(json, BaseDto::class.java)
        val connectedDto = PayloadFactory(baseDto.payload).create<ConnectedDto>(baseDto.action)
        return connectedDto.id
    }

    private suspend fun getServerIpByUDP(): String {
        val socketUDP = DatagramSocket()
        socketUDP.soTimeout = 2000

        val message = "message".toByteArray()

        val packet = DatagramPacket(
            message, message.size,
            InetAddress.getByName("255.255.255.255"), UDP_PORT
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
    }
}