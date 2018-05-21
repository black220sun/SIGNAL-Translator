package com.blacksun.optimizer

import com.blacksun.GrammarGen
import com.blacksun.utils.node.Node

class OptimizeEmpty : Optimization {
    override val name = "?<statements-list>"

    override fun optimization(): ArrayList<(Node) -> Node> =
            arrayListOf(opt1(), opt2(), opt3())

    private fun opt1(): (Node) -> Node {
        val ruleNode = Node("<statements-list>") +
                (Node("<statement>") + Node(";")) +
                Node("?rest")
        return {
            val result = it.match(ruleNode)
            if (result.OK)
                result["?rest"]!!
            else
                it
        }
    }

    private fun opt2(): (Node) -> Node {
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

    private fun opt3(): (Node) -> Node {
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