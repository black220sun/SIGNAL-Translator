package com.blacksun.utils.rule

import com.blacksun.lexer.Lexer

class EmptyRule : Rule("") {
    override val parse = { Lexer.skip() }
    override val computeFirst = { emptyList<Int>() }
}
