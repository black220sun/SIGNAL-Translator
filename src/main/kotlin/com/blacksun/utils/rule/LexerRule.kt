package com.blacksun.utils.rule

import com.blacksun.Lexer
import com.blacksun.Logger
import com.blacksun.utils.node.Node

class LexerRule(private val code: Int) : Rule(code.toChar().toString()) {
    override val parse = {
        Logger.info("Parsing LexerRule $name")
        if (Lexer.read() == code) {
            Logger.info("Char matched, $name parsed")
            Lexer.add()
        } else {
            Logger.warn("Char mismatch")
        }
        Node()
    }

    override val computeFirst = {
        Logger.info("$name first computed as [$code]")
        arrayListOf(code)
    }
    override val computeNames = {
        Logger.info("$name names computed as emptyList")
        emptyList<String>()
    }
    override fun check(value: Any): Boolean  = value == code
}
