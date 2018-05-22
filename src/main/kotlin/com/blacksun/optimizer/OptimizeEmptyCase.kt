package com.blacksun.optimizer

import com.blacksun.GrammarGen
import com.blacksun.utils.node.Node

class OptimizeEmptyCase : Optimization {
    override val name = "?<statements-list>"

    override fun optimization(): (Node) -> Node {
        val ruleNode = Node("<statements-list>") +
                GrammarGen.parse("CASE B[10] OF ENDCASE;", "<statement>")
                        .template("B[10]", "<expression>") +
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