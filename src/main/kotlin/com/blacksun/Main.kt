package com.blacksun

import com.blacksun.utils.GrammarGen
import com.blacksun.utils.node.MatcherRules
import com.blacksun.utils.node.Node
import java.io.File

fun main(args: Array<String>) {
    GrammarGen.initGrammar("grammar.gr")
    val node = GrammarGen.parse(File("res/test.sig"))
    node.print()
    val rules = MatcherRules()
    rules["?TEST"] = { Node(it.token.rename("TES4")) }
    rules["!<block>"] = { Node("<empty>") }
    node.rewrite(rules).root.print()
}
