package com.blacksun.utils

class Token(
        private val row: Int,
        private val col: Int,
        private val builder: StringBuilder = StringBuilder()
) {
    private val name by lazy(builder::toString)

    constructor(row: Int, col: Int, name: String) : this(row, col, StringBuilder(name))

    operator fun plusAssign(char: Int) {
        builder.append(char.toChar())
    }

    override fun toString() = "$row\t$col\t$name"
    override fun equals(other: Any?) =
            if (other is Token)
                toString() == other.toString()
            else
                false
}
