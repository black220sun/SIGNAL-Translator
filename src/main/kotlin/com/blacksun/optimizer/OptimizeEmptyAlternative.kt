package com.blacksun.optimizer

import com.blacksun.GrammarGen
import com.blacksun.utils.node.Node

class OptimizeEmptyAlternative : Optimization {
    override val name = "?<alternatives-list>"

    override fun optimization(): (Node) -> Node {
        val ruleNode = Node("<alternatives-list>") +
                GrammarGen.parse("B[10]: /\\", "<alternative>")
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