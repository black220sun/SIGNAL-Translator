package com.blacksun.utils.node

import com.blacksun.utils.node.Node as N

class NodeMatcher: HashMap<String, N>() {
    private var errorCounter = 0
    private lateinit var lateRoot: N
    val root by lazy { lateRoot }
    fun error(node: N) {
        put("?error$errorCounter", node)
        ++errorCounter
    }
    fun setRoot(node: N) {
        if (!::lateRoot.isInitialized)
            lateRoot = node
    }
}