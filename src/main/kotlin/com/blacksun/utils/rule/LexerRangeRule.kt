package com.blacksun.utils.rule

import com.blacksun.Lexer
import com.blacksun.Logger
import com.blacksun.utils.node.Node

class LexerRangeRule(range: List<Int>) : Rule("'${range[0].toChar()}'..'${range[1].toChar()}'") {
    override val parse = {
        Logger.info("Parsing LexerRangeRule $name")
        if (Lexer.read() in first) {
            Logger.info("Char matched, $name parsed")
            Lexer.add()
        } else {
            Logger.warn("Char mismatch")
        }
        Node()
    }

    override val computeFirst = {
        val first = (range[0]..range[1]).toList()
        Logger.info("$name first computed as $first")
        first
    }
    override val computeNames = {
        Logger.info("$name names computed as emptyList")
        emptyList<String>()
    }
    override fun check(value: Any): Boolean  = value in first
}
