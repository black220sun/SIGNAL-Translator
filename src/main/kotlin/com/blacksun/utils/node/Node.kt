package com.blacksun.utils.node

import com.blacksun.utils.GrammarGen
import com.blacksun.utils.Token

private const val defaultValue = "error"

class Node(val value: Any = defaultValue): Cloneable {
    private val children = ArrayList<Node>()
    val token: Token by lazy {
        when {
            value is Token -> value
            children.isEmpty() -> Token()
            else -> children[0].token
        }

    }
    operator fun plus(node: Node): Node {
        this += node
        return this
    }
    operator fun plus(nodes: List<Node>): Node {
        this += nodes
        return this
    }
    operator fun plusAssign(node: Node) {
        if (node.value != defaultValue)
            children.plusAssign(node)
    }
    fun print(depth: Int = 0) {
        for (i in 0 until depth)
            print('\t')
        println(value)
        val newDepth = depth + 1
        for (child in children)
            child.print(newDepth)
    }

    fun template(pattern: String, rule: String, name: String): Node {
        val node = GrammarGen.parse(pattern, rule)
        val rules = MatcherRules()
        val opt: (Node) -> Node = {
            if (it.match(node).OK)
                Node(name)
            else
                it
        }
        rules["?$rule"] = arrayListOf(opt)
        return rewrite(rules)
    }

    fun rewrite(rules: MatcherRules): Node {
        val children = children.map { it.rewrite(rules) }.toTypedArray()
        val name = (value as? Token)?.name ?: value
        val nameExt = "?$name"
        val node = Node(value)
        node.children += children
        return if (nameExt in rules) {
            var tmp = node
            val optimizations = rules[nameExt]!!
            optimizations.forEach { tmp = it(tmp) }
            tmp
        } else
            node
    }

    fun match(node: Node): NodeMatcher {
        val table = NodeMatcher()
        match(node, table)
        return table
    }

    private fun match(node: Node, table: NodeMatcher) {
        val other = node.value
        if (other is String && other[0] == '?')
            table[other] = this
        else if (value == other && children.size == node.children.size)
            children.forEachIndexed { index, child -> child.match(node.children[index], table) }
        else
            table.error(this)
    }

    fun traverse(func: (Node) -> Unit, pre: Boolean = true,
                 stop: (Node) -> Boolean = { false },
                 filter: (Node) -> Boolean = { true }) {
        if (pre)
            traversePre(func, stop, filter)
        else
            traversePost(func, stop, filter)
    }

    private fun traversePre(func: (Node) -> Unit, stop: (Node) -> Boolean, filter: (Node) -> Boolean) {
        if (stop(this))
            return
        if (filter(this))
            func(this)
        children.forEach { it.traversePre(func, stop, filter) }
    }

    private fun traversePost(func: (Node) -> Unit, stop: (Node) -> Boolean, filter: (Node) -> Boolean) {
        if (stop(this))
            return
        children.forEach { it.traversePost(func, stop, filter) }
        if (filter(this))
            func(this)
    }

    operator fun plusAssign(nodes: List<Node>) = children.plusAssign(nodes.filter { it.value != defaultValue })

    override fun toString() = "Node($value)"

    override fun equals(other: Any?): Boolean =
            when {
                other !is Node ->
                    false
                value != other.value ->
                    false
                else -> children == other.children
            }

    override fun hashCode(): Int {
        var result = value.hashCode()
        result = 31 * result + children.hashCode()
        return result
    }
}