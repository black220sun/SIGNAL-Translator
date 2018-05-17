package com.blacksun.utils.rule

import com.blacksun.utils.Node

class RuleAlternative(rule: String) {
    private val rules = ArrayList<Rule>()
    val first by lazy { rules[0].first }
    val names by lazy { rules[0].names }
    val empty by lazy { rules[0] is EmptyRule || rules[0].empty }

    init {
        for (part in rule.split(' ', '\t'))
            rules += Rule[part.trim()]
    }
    fun parse(): List<Node> =
            if (empty) {
                arrayListOf(rules[0].parse())
            } else
                rules.map { it.parse() }
    fun check(char: Int) = empty || char in first
    fun check(node: Node) = empty || node.value in names || node.token.name in names
    override fun toString() = rules.joinToString(" ")
}