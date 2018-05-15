package com.blacksun

import com.blacksun.utils.GrammarGen

fun main(args: Array<String>) {
    GrammarGen.initGrammar("grammar_test")
    GrammarGen.print()
    GrammarGen.parse("test.sig").print()
}
