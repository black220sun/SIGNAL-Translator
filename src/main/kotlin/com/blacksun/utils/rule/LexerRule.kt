package com.blacksun.utils.rule

import com.blacksun.lexer.Lexer
import com.blacksun.utils.Node

class LexerRule(private val code: Int) : Rule(code.toChar().toString()) {
    override val parse = {
        println("Lexer rule $name")
        if (Lexer.read() == code)
            Lexer.add()
        Node()
    }

    override val computeFirst = { arrayListOf(code) }
    override val computeNames = { emptyList<String>() }
}
