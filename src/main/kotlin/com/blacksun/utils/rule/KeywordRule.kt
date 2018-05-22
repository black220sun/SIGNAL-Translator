package com.blacksun.utils.rule

import com.blacksun.Lexer
import com.blacksun.GrammarGen
import com.blacksun.utils.node.Node

class KeywordRule(name: String) : Rule(name) {
    init {
        GrammarGen.registerKeyword(name)
    }
    override val parse = {
        var tokenNode = Lexer.getTokenNode()
        if (tokenNode == null) {
            Lexer.createTokenNode()
            tokenNode = Lexer.getTokenNode()!!
        }
        val token = tokenNode.token
        if (token.name == name)
            Node(token)
        else {
            GrammarGen.error()
            val name1 = if (token.name.isBlank())
                Lexer.char.toChar().toString()
            else
                token.name
            System.err.println("Error ${Lexer.errorMsg()}: expected $name, found $name1")
            Node()
        }
    }
    override val computeFirst = { emptyList<Int>() }
    override val computeNames = { arrayListOf(name) }
    override fun check(value: Any) = (value as? Node)?.token?.name == name
}
