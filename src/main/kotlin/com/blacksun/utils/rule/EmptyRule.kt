package com.blacksun.utils.rule

import com.blacksun.lexer.Lexer
import com.blacksun.utils.Node

class EmptyRule : Rule("") {
    override val empty = true
    override val parse = { Lexer.skip(); Node() }
    override val computeFirst = { emptyList<Int>() }
    override val computeNames = { emptyList<String>() }
}
