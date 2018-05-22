package com.blacksun.optimizer

import com.blacksun.GrammarGen
import com.blacksun.utils.node.MatcherRules
import com.blacksun.utils.node.Node

class OptimizeEmptyThen : Optimization {
    override val name = "?<statement>"

    override fun optimization(): (Node) -> Node {
        val template = GrammarGen
                .parse("IF A=A THEN ELSE B(); ENDIF;", "<statement>")
                .template("A=A;", "<conditional-expression>")
                .template("B();", "<statements-list>")
        val rewriteRules = MatcherRules()
        return {
            val result = it.match(template)
            if (result.OK) {
                val opt: (Node) -> Node = {
                    Node("<conditional-expression>") +
                            (Node("<logical-summand>") +
                                    (Node("<logical-multiplier>") +
                                            Node("NOT") +
                                            (Node("<logical-multiplier>") +
                                                    Node("[")
                                                    + result["?<conditional-expression>"]!! +
                                                    Node("]"))) +
                                    (Node("<logical-multipliers-list>") +
                                            Node("<empty>"))) +
                            (Node("<logical>") +
                                    Node("<empty>"))
                }
                val opt2: (Node) -> Node = {result["?<statements-list>"]!!}
                val opt3: (Node) -> Node = {GrammarGen.parse("", "<alternative-part>")}
                rewriteRules["?<conditional-expression>"] = arrayListOf(opt)
                rewriteRules["?<statements-list>"] = arrayListOf(opt2)
                rewriteRules["?<alternative-part>"] = arrayListOf(opt3)
                it.rewrite(rewriteRules)
            } else
                it
        }
    }
}