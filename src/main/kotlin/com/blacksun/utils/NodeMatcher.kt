package com.blacksun.utils

import com.blacksun.utils.Node as N

class NodeMatcher: HashMap<String, N>() {
    private var errorCounter = 0
    fun error(node: N) {
        put("?error$errorCounter", node)
        ++errorCounter
    }
}