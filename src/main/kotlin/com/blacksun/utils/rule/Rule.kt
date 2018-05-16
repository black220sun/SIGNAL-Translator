package com.blacksun.utils.rule

import com.blacksun.utils.Node

abstract class Rule(private val name: String) {
    val first by lazy { computeFirst() }
    val names by lazy { computeNames() }
    open val empty = false
    abstract val parse: () -> Node
    protected abstract val computeFirst: () -> List<Int>
    protected abstract val computeNames: () -> List<String>
    companion object {
        @JvmStatic
        operator fun get(name: String): Rule =
            when {
                name.matches("'.'") -> LexerRule(name[1].toInt())
                name.matches("\\d+") -> LexerRule(name.toInt())
                name.matches("'.'\\.\\.'.'") -> LexerRangeRule(name.split("..").map { it[1].toInt() })
                name.matches("\\d+\\.\\.\\d+") -> LexerRangeRule(name.split("..").map(String::toInt))
                name.matches("<[-a-zA-Z]+>") -> PartRule(name)
                name.isEmpty() -> EmptyRule()
                else -> TODO()
            }
        private fun String.matches(regex: String): Boolean = matches(Regex(regex))
    }
    override fun toString() = name
}
