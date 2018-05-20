package com.blacksun

import com.blacksun.optimizer.OptimizeEmpty
import com.blacksun.optimizer.Optimizer
import com.blacksun.utils.GrammarGen
import java.io.File

fun main(args: Array<String>) {
    GrammarGen.initGrammar("grammar.gr")
    val node = GrammarGen.parse(File("res/test.sig"))
    node.print()

    (Optimizer() + OptimizeEmpty()).process(node).print()
}
