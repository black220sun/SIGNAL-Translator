package com.blacksun.utils

class RulePart(private val name: String) {
    private val first = ArrayList<Int>()
    private var computed = false

    fun computeFirst(): ArrayList<Int> {
        if (computed)
            return first
        when {
            name.matches(Regex("'.'\\.\\.'.'")) -> {
                val parts = name.split("..").map { it[1].toInt() }
                first += (parts[0]..parts[1])
            }
            name.matches(Regex("'.+'")) -> first += TokenCode[name.drop(1).dropLast(1)]
            name.matches(Regex("<.+>")) -> first += GrammarGen[name].computeFirst()
        }
        computed = true
        return first
    }

    fun print() {
        print(name)
        print(" ")
    }
}
