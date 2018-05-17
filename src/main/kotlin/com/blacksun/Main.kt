package com.blacksun

import com.blacksun.lexer.Lexer
import com.blacksun.utils.GrammarGen

fun main(args: Array<String>) {
    GrammarGen.initGrammar("grammar_test")
    Lexer.init("res/test.sig")
    GrammarGen["<multi-delimiter>"].parse().print()
    GrammarGen["<multi-delimiter>"].parse().print()
}
