package org.gustavolyra.p2p

import java.util.concurrent.atomic.AtomicBoolean

class BlockChain {

    private var blocks: MutableList<Block> = mutableListOf()

    private val difficulty = 10
    private val validPrefix = "0".repeat(difficulty)


    fun add(block: Block): Block {
        blocks.add(block)
        return block
    }

    fun getLastBlock(): Block {
        return blocks.last()
    }

    private fun isMined(block: Block): Boolean {
        return block.hash.startsWith(validPrefix)
    }

    @Synchronized
    fun mine(block: Block): Block {
        val isRunning = AtomicBoolean(true)
        printMiningMsgOnConsole(isRunning)
        var minedBlock = block.copy()
        while (!isMined(minedBlock)) {
            minedBlock = minedBlock.copy(nonce = minedBlock.nonce + 1)
        }
        isRunning.set(false)
        println("Block mined! 🎉 $minedBlock")
        return minedBlock
    }

    private fun printMiningMsgOnConsole(isRunning: AtomicBoolean) {
        Thread.startVirtualThread(Runnable {
            var i = 0
            while (isRunning.get()) {
                Thread.sleep(300)
                print("\rMining block ⛏${".".repeat(i)}")
                if (i == 3) i = 0
                i++
            }
        })
    }


}