package com.blacksun.optimizer

import com.blacksun.utils.node.Node

interface Optimization {
    val name: String
    fun optimization(): (Node) -> Node
}
