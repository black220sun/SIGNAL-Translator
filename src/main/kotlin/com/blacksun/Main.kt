package com.blacksun

import com.blacksun.utils.GrammarGen

fun main(args: Array<String>) {
    GrammarGen.initGrammar("grammar.gr")
    val node = GrammarGen.parse("PROGRAM TEST; BEGIN IF A=A THEN A:=10;ENDIF;END.")
    node.print()
    val pattern = node.template("A=A;", "<conditional-expression>", "?cond-expr")
    pattern.print()
    node.match(pattern)["?cond-expr"]!!.print()
}
