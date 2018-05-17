package com.blacksun.utils.rule

import com.blacksun.lexer.Lexer
import com.blacksun.utils.Node

class KeywordRule(name: String) : Rule(name) {
    override val parse = {
        var tokenNode = Lexer.getTokenNode()
        if (tokenNode == null) {
            Lexer.createTokenNode()
            tokenNode = Lexer.getTokenNode()!!
        }
        val token = tokenNode.token
        if (token.name == name)
            Node(token)
        else
            error("")
    }
    override val computeFirst = { emptyList<Int>() }
    override val computeNames = { arrayListOf(name) }

}
