package com.blacksun.optimizer

import com.blacksun.GrammarGen
import com.blacksun.utils.node.Node

class OptimizeEmptyLoop : Optimization {
    override val name = "?<statements-list>"

    override fun optimization(): (Node) -> Node {
        val ruleNode = Node("<statements-list>") +
                GrammarGen.parse("LOOP ENDLOOP;", "<statement>") +
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