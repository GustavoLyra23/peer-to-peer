package org.gustavolyra

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

    private fun mine(block: Block)



}