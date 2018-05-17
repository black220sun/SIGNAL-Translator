package com.blacksun

import com.blacksun.utils.GrammarGen

fun main(args: Array<String>) {
    GrammarGen.initGrammar("grammar.gr")
    GrammarGen.parse("res/test.sig")
}
