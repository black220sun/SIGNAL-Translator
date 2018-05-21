package com.blacksun.utils.rule

import com.blacksun.Lexer
import com.blacksun.utils.node.Node

class LexerRule(private val code: Int) : Rule(code.toChar().toString()) {
    override val parse = {
        if (Lexer.read() == code)
            Lexer.add()
        Node()
    }

    override val computeFirst = { arrayListOf(code) }
    override val computeNames = { emptyList<String>() }
    override fun check(value: Any): Boolean  = value == code
}
