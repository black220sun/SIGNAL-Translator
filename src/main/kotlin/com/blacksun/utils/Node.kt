package com.blacksun.utils

private const val defaultValue = ""

class Node(val value: Any = defaultValue) {
    private val children = ArrayList<Node>()
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
}