package com.blacksun

import com.blacksun.utils.GrammarGen
import java.io.File

fun main(args: Array<String>) {
    GrammarGen.initGrammar("grammar.gr")
    File("res").listFiles{ _, s -> s.endsWith(".txt") }.forEach {
        GrammarGen.parse(it).print()
    }
}
