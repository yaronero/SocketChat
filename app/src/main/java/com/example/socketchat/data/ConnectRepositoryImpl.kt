package com.example.socketchat.data

import com.example.socketchat.domain.ConnectRepository
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.Socket

class ConnectRepositoryImpl : ConnectRepository {

    override suspend fun connectToServer() {
        try {

            val address = getServerIpByUDP()
            val socketTCP = Socket(address, TCP_PORT)
        } catch (e: Exception) {
            e.printStackTrace()
        }
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