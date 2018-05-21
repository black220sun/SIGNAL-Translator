package com.blacksun.utils.rule

import com.blacksun.Lexer
import com.blacksun.utils.node.Node

class LexerRangeRule(range: List<Int>) : Rule("'${range[0].toChar()}'..'${range[1].toChar()}'") {
    override val parse = {
        if (Lexer.read() in first)
            Lexer.add()
        Node()
    }

    override val computeFirst = {
        (range[0]..range[1]).toList()
    }
    override val computeNames = { emptyList<String>() }
    override fun check(value: Any): Boolean  = value in first
}
