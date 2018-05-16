package com.blacksun

import com.blacksun.lexer.Lexer
import com.blacksun.utils.GrammarGen
import com.blacksun.utils.Node

fun main(args: Array<String>) {
    GrammarGen.initGrammar("grammar_test")
    Lexer.init("test.sig")
    (GrammarGen.first.parse()).print()
}
