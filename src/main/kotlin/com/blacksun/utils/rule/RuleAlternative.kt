package com.blacksun.utils.rule

class RuleAlternative(rule: String) {
    private val rules = ArrayList<Rule>()
    val first by lazy { rules[0].first }
    init {
        for (part in rule.split(' ', '\t'))
            rules += Rule[part.trim()]
    }
    fun parse(): Any = rules.map { it.parse() }
    override fun toString() = rules.joinToString(" ")
    fun check(char: Int) = first.isEmpty() || char in first
}