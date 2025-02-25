package org.gustavolyra.p2p

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.security.MessageDigest

fun main() = runBlocking {
    //mocking the peers...
    val cachedPeer = createPeer()
    val peerList = mutableListOf<Peer>(cachedPeer, createPeer("localhost", 8081), createPeer("localhost", 8080))
    val serverJob = runServer(8080, peerList)
    delay(2000)
    val newPeers = getPeers(peerList[2])
    println("Peer obtained ->  $newPeers")

    //BlockChain...
    val genesisBlock = Block(previousHash = "0", data = "Genesis Block")
    val blockChain = BlockChain()
    val minedBlock = blockChain.mine(genesisBlock)
    blockChain.add(minedBlock)


    serverJob.join()
}

fun String.hash(algorithm: String = "SHA=256"): String {
    val msgDigest = MessageDigest.getInstance(algorithm)
    msgDigest.update(this.toByteArray())
    return String.format("%064x", java.math.BigInteger(1, msgDigest.digest()))
}

