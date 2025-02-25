package org.gustavolyra

import kotlinx.coroutines.internal.synchronized

class BlockChain {

    private var blocks: MutableList<Block> = mutableListOf()

    private val difficulty = 5
    private val validPrefix = "0".repeat(difficulty)


    fun add(block: Block): Block {
        blocks.add(block)
        return block
    }

    private fun isMined(block: Block): Boolean {
        return block.hash.startsWith(validPrefix)
    }

    @Synchronized
    private fun mine(block: Block): Block {
        printMiningMsgOnConsole()
        var minedBlock = block.copy()
        while (!isMined(minedBlock)) {
            minedBlock = minedBlock.copy(nonce = minedBlock.nonce + 1)
        }
        println("Block mined! 🎉 $minedBlock")
        return minedBlock
    }

    private fun printMiningMsgOnConsole() {
        Thread.startVirtualThread(Runnable {
            var i = 1
            while (true) {
                Thread.sleep(300)
                println("\rMining block ⛏${".".repeat(i)}")
                if (i == 3) i = 1
                i++
            }
        })
    }


}