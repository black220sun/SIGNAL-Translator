package com.blacksun.utils.rule

import com.blacksun.utils.node.Node

class RuleAlternative(rule: String) {
    private val rules = ArrayList<Rule>()
    val first by lazy { rules[0].first }
    val names by lazy {
        val names = ArrayList<String>()
        for (r in  rules) {
            names += r.names
            if (!r.empty)
                break
        }
        names.toSet().toList()
    }
    val empty by lazy { rules.all { it.empty } }

    init {
        for (part in rule.split(' ', '\t'))
            rules += Rule[part.trim()]
    }
    fun parse(): List<Node> = rules.map { it.parse() }
    fun check(value: Any) = rules[0].check(value)
    override fun toString() = rules.joinToString(" ")
}