package org.gustavolyra.p2p

import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket
import java.net.ConnectException
import kotlin.random.Random

data class Peer(val host: String, val port: Int)

suspend fun getPeers(peer: Peer): List<Peer> = withContext(Dispatchers.IO) {
    try {
        val socket = Socket(peer.host, peer.port)
        val writer = PrintWriter(socket.getOutputStream(), true)
        val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
        writer.println("GET_PEERS")
        val response = reader.readLine()
        val peers = response?.split(",")?.mapNotNull {
            val parts = it.split(":")
            if (parts.size == 2) Peer(parts[0], parts[1].toIntOrNull() ?: return@mapNotNull null)
            else null
        } ?: emptyList()

        socket.close()
        peers
    } catch (e: ConnectException) {
        println("Failed to connect to peer ${peer.host}:${peer.port}")
        emptyList()
    } catch (e: Exception) {
        println("Error while getting peers: ${e.message}")
        emptyList()
    }
}


fun runServer(port: Int, knownPeers: MutableList<Peer>): Job {
    return GlobalScope.launch(Dispatchers.IO) {
        try {
            val serverSocket = ServerSocket(port)
            println("Server started on port $port")
            while (true) {
                val client: Socket = serverSocket.accept()
                launch {
                    try {
                        val reader = BufferedReader(InputStreamReader(client.getInputStream()))
                        val writer = PrintWriter(client.getOutputStream(), true)
                        val request = reader.readLine()
                        if (request == "GET_PEERS") {
                            val response = knownPeers.joinToString(",") { "${it.host}:${it.port}" }
                            writer.println(response)
                        } else if (request == "MINE_BLOCK") {
                            var peer = knownPeers[Random.nextInt(0, knownPeers.size)]


                        }
                    } catch (e: Exception) {
                        println("Error handling client: ${e.message}")
                    } finally {
                        client.close()
                    }
                }
            }
        } catch (e: Exception) {
            println("Server error: ${e.message}")
        }
    }
}

fun createPeer(): Peer {
    return Peer(
        "localhost",
        Random.nextInt(1000, 9999)
    )
}

fun createPeer(host: String, port: Int): Peer {
    return Peer(host, port)
}