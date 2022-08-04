package com.example.socketchat.data

import com.example.socketchat.domain.ConnectRepository
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.Socket

class ConnectRepositoryImpl : ConnectRepository {

    override suspend fun connectToServer() {
        try {
            val socketUDP = DatagramSocket()
            socketUDP.soTimeout = 5000

            val message = "message".toByteArray()
            var address = ""
            while (address.isEmpty()) {
                try {
                    address = getServerIpByUDP(socketUDP, message) ?: ""
                    val socketTCP = Socket(address, TCP_PORT)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun getServerIpByUDP(socketUDP: DatagramSocket, message: ByteArray): String? {
        val packet = DatagramPacket(
            message, message.size,
            InetAddress.getByName("255.255.255.255"), UDP_PORT
        )
        socketUDP.send(packet)
        socketUDP.receive(packet)

        return packet.address.hostAddress
    }

    companion object {
        private const val UDP_PORT = 8888
        private const val TCP_PORT = 6666
    }
}