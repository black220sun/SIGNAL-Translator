package com.blacksun.utils

import com.blacksun.lexer.Lexer

class RulePart(private val name: String) {
    private val first = ArrayList<Int>()
    private var computed = false
    private val func: () -> Node by lazy (when {
        name.matches(Regex("('.'\\.\\.'.')|\\d+")) ->  {{{ Lexer.add(); Node() }}}
        name.matches(Regex("'.+'")) ->   {{{ Node(Lexer.getToken())}}}
        name.matches(Regex("<.+>")) ->  {{{ GrammarGen[name].parse() }}}
        else -> {{{ Node() }}}
    })

    fun computeFirst(): ArrayList<Int> {
        if (computed)
            return first
        computed = true
        when {
            name.matches(Regex("'.'\\.\\.'.'")) -> {
                val parts = name.split("..").map { it[1].toInt() }
                first += (parts[0]..parts[1])
            }
            name.matches(Regex("'.+'")) -> {
                first += TokenCode[name.drop(1).dropLast(1)]
            }
            name.matches(Regex("<.+>")) -> {
                first += GrammarGen[name].computeFirst()
            }
            name.matches(Regex("\\d+")) -> {
                first += name.toInt()
            }
            name.isEmpty() -> {}
            else -> error("Unsupported rule part: $name")
        }
        return first
    }

    fun print() {
        print(name)
        print(" ")
    }

    fun parse() = func()
}
