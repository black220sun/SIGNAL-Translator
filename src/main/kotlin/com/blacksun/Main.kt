package com.blacksun

import com.blacksun.lexer.Lexer
import com.blacksun.utils.GrammarGen

fun main(args: Array<String>) {
    GrammarGen.initGrammar("grammar.gr")
    Lexer.init("res/test.sig")
    GrammarGen.first.parse().print()
}
