package com.blacksun.utils

class Rule(rule: String) {
    private val parts = ArrayList<RulePart>()
    private lateinit var first: ArrayList<Int>
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

    fun computeFirst(): ArrayList<Int> {
        first = parts[0].computeFirst()
        return first
    }
}
