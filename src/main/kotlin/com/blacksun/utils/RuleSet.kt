package com.blacksun.utils

class RuleSet(private val name: String, parts: List<String>) {
    private val rules = ArrayList<Rule>()
    private val empty: Boolean

    init {
        for (part in parts)
            rules += Rule(part)
        empty = rules.any(Rule::isEmpty)
    }

    fun isEmpty() = empty

    fun computeFirst(): ArrayList<Int> {
        val first = ArrayList<Int>()
        rules.forEach { first += it.computeFirst() }
        return first
    }

    fun print() {
        print(name)
        print(" --> ")
        for (rule in rules.dropLast(1)) {
            rule.print()
            print("| ")
        }
        rules.last().print()
        println(";")
    }
}
