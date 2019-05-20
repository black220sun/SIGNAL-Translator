package com.blacksun.optimizer

import com.blacksun.utils.node.Node

class OptimizeEmptyWhile : Optimization {
    override val name = "?<statements-list>"

    override fun optimization(): (Node) -> Node {
        val ruleNode = Node("<statements-list>") +
                (Node("<statement>") +
                        Node("WHILE") +
                        Node("?condition") +
                        Node("DO") +
                        (Node("<statements-list>") + Node("<empty>")) +
                        Node("ENDWHILE") +
                        Node(";")) +
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