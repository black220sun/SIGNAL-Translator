package com.blacksun.optimizer

import com.blacksun.GrammarGen
import com.blacksun.utils.node.Node

class OptimizeEmptyElse : Optimization {
    override val name = "?<statement>"

    override fun optimization(): (Node) -> Node {
        val ruleNode = Node("<statement>") +
                (Node("<condition-statement>") +
                        Node("?<incomplete-conditional-statement>") +
                        GrammarGen.parse("ELSE", "<alternative-part>")) +
                Node("ENDIF") +
                Node(";")
        return {
            val result = it.match(ruleNode)
            if (result.OK)
                Node("<statement>") +
                        (Node("<condition-statement>") +
                                result["?<incomplete-conditional-statement>"]!! +
                                GrammarGen.parse("", "<alternative-part>")) +
                        Node("ENDIF") +
                        Node(";")
            else
                it
        }
    }
}