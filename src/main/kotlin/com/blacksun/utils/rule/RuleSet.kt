package com.blacksun.utils.rule

import com.blacksun.lexer.Lexer
import com.blacksun.utils.Node

class RuleSet(private val name: String, rule: String) {
    private val rules = ArrayList<RuleAlternative>()
    val first by lazy(::computeFirst)

    init {
        for (part in rule.split('|'))
            rules += RuleAlternative(part.trim())
    }

    private fun computeFirst(): ArrayList<Int> {
        val first = ArrayList<Int>()
        for (rule in rules)
            first += rule.first
        return first
    }

    fun parse(): Any {
        val char = Lexer.read()
        val node = Node(name)
        for (rule in rules)
            if (rule.check(char)) {
                rule.parse()
                if (name.matches(Regex("<[-a-z]+>")))
                    node += Node(Lexer.getToken())
                return node
        }
        error("Unmatched symbol '${char.toChar()}' ($char) for $name rule")
    }

    override fun toString() = "$name --> " + rules.joinToString(" | ") + ';'
}