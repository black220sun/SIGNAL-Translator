package com.blacksun.utils

class Rule(private val name: String, private val type: String, rule: String) {
    private val parts = ArrayList<RulePart>()
    val first by lazy { parts[0].computeFirst() }
    private val empty: Boolean

    init {
        val rules = rule.split(" ", "\t").filter(String::isNotEmpty)
        if (rules.isEmpty()) {
            empty = true
            parts += RulePart("")
        } else {
            empty = false
            for (part in rules)
                parts += RulePart(part)
        }
    }

    fun isEmpty() = empty

    fun print() {
        for (part in parts)
            part.print()
    }

    fun parse(): Node {
        val node = Node(rule=name)
        parts.forEach { node += it.parse() }
        return node
    }
}
