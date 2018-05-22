package com.blacksun

fun main(args: Array<String>) {
    GrammarGen.initGrammar("grammar.gr")
    val node = GrammarGen.parse("", "<alternative-part>")
    node.print()
}
