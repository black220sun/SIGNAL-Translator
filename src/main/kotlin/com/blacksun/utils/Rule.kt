package com.blacksun.utils

class Rule(rule: String) {
    private val parts = ArrayList<RulePart>()

    init {
        val rules = rule.split(" ", "\t").filter(String::isNotEmpty)
        if (rules.isEmpty())
            parts += RulePart("")
        else
            for (part in rules)
                parts += RulePart(part)
    }

    fun print() {
        for (part in parts)
            part.print()
    }
}
