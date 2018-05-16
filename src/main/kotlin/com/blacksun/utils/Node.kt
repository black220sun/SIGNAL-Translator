package com.blacksun.utils

class Node(private val value: Any) {
    private val children = ArrayList<Node>()
    operator fun plusAssign(node: Node) = children.plusAssign(node)
    fun print(depth: Int = 0) {
        for (i in 0 until depth)
            print('\t')
        println(value)
        val newDepth = depth + 1
        for (child in children)
            child.print(newDepth)
    }

    operator fun plusAssign(nodes: ArrayList<Node>) = children.plusAssign(nodes)

    override fun toString() = "Node($value)"
}