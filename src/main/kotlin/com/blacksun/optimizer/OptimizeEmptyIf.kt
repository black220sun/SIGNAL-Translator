package com.blacksun.optimizer

import com.blacksun.GrammarGen
import com.blacksun.utils.node.Node

class OptimizeEmptyIf : Optimization {
    override val name = "?<statements-list>"

    override fun optimization(): (Node) -> Node {
        val ruleNode = Node("<statements-list>") +
                GrammarGen.parse("IF A=A THEN ENDIF;", "<statement>")
                        .template("A=A;", "<conditional-expression>") +
                Node("?rest")
        return {
            val result = it.match(ruleNode)
            if (result.OK)
                result["?rest"]!!
            else
                it
        }
    }
}