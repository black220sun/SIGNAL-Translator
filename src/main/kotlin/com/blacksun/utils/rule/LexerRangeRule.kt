package com.blacksun.utils.rule

import com.blacksun.lexer.Lexer
import com.blacksun.utils.Node

class LexerRangeRule(range: List<Int>) : Rule("'${range[0].toChar()}'..'${range[1].toChar()}'") {
    override val parse = {
        println("Lexer range rule $name")
        if (Lexer.read() in first)
            Lexer.add()
        Node()
    }

    override val computeFirst = {
        (range[0]..range[1]).toList()
    }
    override val computeNames = { emptyList<String>() }
}
