package com.blacksun

import com.blacksun.utils.GrammarGen

fun main(args: Array<String>) {
    GrammarGen.initGrammar("grammar.gr")
    GrammarGen["<unsigned-constant>"].empty
    GrammarGen.parse("A := B[10];", "<statement>").print()
}
