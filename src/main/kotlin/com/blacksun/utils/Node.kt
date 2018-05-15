package com.blacksun.utils

class Node(private val token: Token? = null, private val rule: String? = null) {
    private val children = ArrayList<Node>()

    operator fun plusAssign(node: Node) = children.plusAssign(node)

    fun print(depth: Int = 0) {
        for (i in 0 until depth)
            print('\t')
        println(rule ?: token)
        val newDepth = depth + 1
        for (child in children)
            child.print(newDepth)
    }
}