package com.blacksun.utils

class Token(private val builder: StringBuilder = StringBuilder()) {
    val name by lazy(builder::toString)

    constructor(name: String) : this(StringBuilder(name))

    operator fun plusAssign(char: Int) {
        builder.append(char.toChar())
    }

    override fun toString() = name
    override fun equals(other: Any?) =
            when (other) {
                is Token -> name == other.name
                is String -> name == other
                else -> false
            }

    override fun hashCode(): Int {
        return builder.hashCode()
    }
}
