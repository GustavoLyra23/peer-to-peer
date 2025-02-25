package org.gustavolyra

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket


data class Peer(val host: String, val port: Int)

suspend fun getPeers(peer: Peer): List<Peer> = withContext(Dispatchers.IO) {
    val socket = Socket(peer.host, peer.port)
    val writer = PrintWriter(socket.getOutputStream(), true)
    val reader = BufferedReader(InputStreamReader(socket.getInputStream()))

    writer.println("GET_PEERS")
    val response = reader.readLine()
    val peers = response.split(",").mapNotNull {
        val parts = it.split(":")
        if (parts.size == 2) Peer(parts[0], parts[1].toIntOrNull() ?: return@mapNotNull null)
        else null
    }
    socket.close()
    peers
}

fun runServer(port: Int, knownPeers: MutableList<Peer>) {
    GlobalScope.launch(Dispatchers.IO) {
        val serverSocket = ServerSocket(port)
        println("Servidor iniciado na porta $port")
        while (true) {
            val client: Socket = serverSocket.accept()
            launch {
                val reader = BufferedReader(InputStreamReader(client.getInputStream()))
                val writer = PrintWriter(client.getOutputStream(), true)
                val request = reader.readLine()
                if (request == "GET_PEERS") {
                    val response = knownPeers.joinToString(",") { "${it.host}:${it.port}" }
                    writer.println(response)
                }
                client.close()
            }
        }
    }
}