package com.blacksun.utils

class Token(
        private val row: Int,
        private val col: Int
) {
    private val builder = StringBuilder()
    private val name by lazy(builder::toString)
//    val code by lazy { TokenCode[name] }

    operator fun plusAssign(char: Int) {
        builder.append(char.toChar())
    }

    override fun toString() = "$row\t$col\t$name"
}
