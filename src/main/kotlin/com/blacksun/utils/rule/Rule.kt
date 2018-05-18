package com.blacksun.utils.rule

import com.blacksun.utils.Node

abstract class Rule(protected val name: String) {
    val first by lazy { computeFirst() }
    val names by lazy { computeNames() }
    open val empty = false
    abstract val parse: () -> Node
    protected abstract val computeFirst: () -> List<Int>
    protected abstract val computeNames: () -> List<String>
    abstract fun check(value: Any): Boolean
    companion object {
        @JvmStatic
        operator fun get(name: String): Rule =
            when {
                name.matches("'.'") -> LexerRule(name[1].toInt())
                name.matches("\\d+") -> LexerRule(name.toInt())
                name.matches("'.'\\.\\.'.'") -> LexerRangeRule(name.split("..").map { it[1].toInt() })
                name.matches("\\d+\\.\\.\\d+") -> LexerRangeRule(name.split("..").map(String::toInt))
                name.matches("<[-a-zA-Z?0-9]+>") -> PartRule(name)
                name.isEmpty() -> EmptyRule()
                else -> KeywordRule(name)
            }
        private fun String.matches(regex: String): Boolean = matches(Regex(regex))
    }
    override fun toString() = name
}

