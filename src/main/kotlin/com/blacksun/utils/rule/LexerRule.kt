package com.blacksun.utils.rule

import com.blacksun.lexer.Lexer

class LexerRule(private val code: Int) : Rule(code.toChar().toString()) {
    override val parse = {
        if (Lexer.read() == code)
            Lexer.add()
        else
            Lexer.skip()
    }

    override val computeFirst = { arrayListOf(code) }
}
