package com.blacksun.utils.rule

import com.blacksun.Lexer
import com.blacksun.GrammarGen
import com.blacksun.Logger
import com.blacksun.utils.node.Node

class KeywordRule(name: String) : Rule(name) {
    init {
        GrammarGen.registerKeyword(name)
    }
    override val parse = {
        Logger.info("Parsing KeywordRule $name")
        var tokenNode = Lexer.getTokenNode()
        if (tokenNode == null) {
            Logger.info("tokenNode is null, creating new tokenNode")
            Lexer.createTokenNode()
            tokenNode = Lexer.getTokenNode()!!
        }
        val token = tokenNode.token
        if (token.name == name) {
            Logger.info("$name parsed")
            Node(token)
        } else {
            GrammarGen.error()
            val foundName = if (token.name.isBlank())
                Lexer.char.toChar().toString()
            else
                token.name
            Logger.err("Error ${Lexer.errorMsg()}: expected $name, found $foundName")
            Node()
        }
    }
    override val computeFirst = {
        Logger.info("$name first computed as emptyList")
        emptyList<Int>()
    }
    override val computeNames = {
        Logger.info("$name names computed as [$name]")
        arrayListOf(name)
    }
    override fun check(value: Any) = (value as? Node)?.token?.name == name
}
