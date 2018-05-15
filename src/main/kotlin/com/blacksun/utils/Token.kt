package com.blacksun.utils

class Token(
    val row: Int,
    val col: Int
) {
    private val builder = StringBuilder()
    val name by lazy(builder::toString)
    val code by lazy { TokenCode[name] }

    operator fun plusAssign(char: Int) {
        builder.append(char.toChar())
    }

    override fun toString() = "$row\t$col\t$code\t$name"
}
