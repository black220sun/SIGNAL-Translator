package com.blacksun.utils

class Token(
        private val row: Int,
        private val col: Int,
        private val builder: StringBuilder = StringBuilder()
) {
    val name by lazy(builder::toString)

    constructor(row: Int, col: Int, name: String) : this(row, col, StringBuilder(name))

    operator fun plusAssign(char: Int) {
        builder.append(char.toChar())
    }

    override fun toString() = "$row\t$col\t$name"
    override fun equals(other: Any?) =
            when (other) {
                is Token -> toString() == other.toString()
                is String -> name == other
                else -> false
            }

    override fun hashCode(): Int {
        var result = row
        result = 31 * result + col
        result = 31 * result + builder.hashCode()
        return result
    }
}
