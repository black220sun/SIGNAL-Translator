package com.blacksun.utils

private const val defaultValue = "error"

class Node(val value: Any = defaultValue) {
    companion object {
        fun fromPattern(text: String): Node {
            return Node()
        }
    }

    private val children = ArrayList<Node>()
    val token: Token by lazy {
        when {
            value is Token -> value
            children.isEmpty() -> Token(0,0)
            else -> children[0].token
        }

    }
    operator fun plus(node: Node): Node {
        this += node
        return node
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

    fun match(node: Node): NodeMatcher {
        val table = NodeMatcher()
        match(node, table)
        return table
    }

    fun match(text: String) = match(Node.fromPattern(text))

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