package com.blacksun

import com.blacksun.lexer.Lexer
import com.blacksun.utils.GrammarGen
import com.blacksun.utils.Node

fun main(args: Array<String>) {
    GrammarGen.initGrammar("grammar_test")
    GrammarGen.print()
    Lexer.init("test.sig")
    (GrammarGen.first.parse() as Node).print()
    (GrammarGen.first.parse() as Node).print()
}
