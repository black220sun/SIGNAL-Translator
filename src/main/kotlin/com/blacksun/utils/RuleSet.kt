package com.blacksun.utils

class RuleSet(private val name: String, parts: List<String>) {
    private val rules = ArrayList<Rule>()

    init {
        for (part in parts)
            rules += Rule(part)
    }

    fun print() {
        print(name)
        print(" --> ")
        if (rules.size == 1)
            rules[0].print()
        else
            for (rule in rules) {
                rule.print()
                print("| ")
            }
        println(";")
    }
}
