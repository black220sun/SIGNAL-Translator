package com.blacksun

import com.blacksun.utils.GrammarGen
import com.blacksun.utils.Node
import java.io.File

fun main(args: Array<String>) {
    GrammarGen.initGrammar("grammar.gr")
    val node = GrammarGen.parse(File("res/test.sig"))
    val matchNode = Node("<signal-program>")
    val program = matchNode + Node("<program>")
    val any = Node("?")
    program + any
    program + Node("?ident")
    program + any
    program + any
    program + any
    val matcher = node.match(matchNode)
    matcher["?ident"]?.print()
}
