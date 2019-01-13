package com.blacksun.utils.rule

import com.blacksun.Lexer
import com.blacksun.Logger
import com.blacksun.utils.node.Node

class EmptyRule : Rule("") {
    override val empty = true
    override val parse = {
        Logger.info("Parsing EmptyRule")
        Lexer.skip();
        Node() }
    override val computeFirst = {
        Logger.info("$name first computed as emptyList")
        emptyList<Int>()
    }
    override val computeNames = {
        Logger.info("$name names computed as emptyList")
        emptyList<String>()
    }
    override fun check(value: Any) = true
}
