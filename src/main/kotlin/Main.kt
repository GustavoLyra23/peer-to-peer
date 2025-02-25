package org.gustavolyra

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.security.MessageDigest

fun main() = runBlocking {
    //mocking the peers...
    val bootstrapPeers = mutableListOf(
        Peer("localhost", 8080),
        Peer("localhost", 8081)
    )
    runServer(8080, bootstrapPeers)
    delay(2000)
    val newPeers = getPeers(Peer("localhost", 8080))
    println("Peers obtidos:  $newPeers")

    val genesisBlock = Block(previousHash = "0", data = "Genesis Block")
    val secondBlock = Block(previousHash = genesisBlock.hash, data = "Second Block")
    val thirdBlock = Block(previousHash = secondBlock.hash, data = "Third Block")
    println(genesisBlock)
    println(secondBlock)
    println(thirdBlock)

}

fun String.hash(algorithm: String = "SHA=256"): String {
    val msgDigest = MessageDigest.getInstance(algorithm)
    msgDigest.update(this.toByteArray())
    return String.format("%064x", java.math.BigInteger(1, msgDigest.digest()))
}

