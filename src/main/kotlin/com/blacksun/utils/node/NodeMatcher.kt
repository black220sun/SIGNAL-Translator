package com.blacksun.utils.node

import com.blacksun.utils.node.Node as N

class NodeMatcher: HashMap<String, N>() {
    private var errorCounter = 0
    val OK by lazy { errorCounter == 0 }
    fun error(node: N) {
        put("?error$errorCounter", node)
        ++errorCounter
    }
}