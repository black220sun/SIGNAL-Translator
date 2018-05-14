package com.blacksun.utils

class Token(
    private val row: Int,
    private val col: Int,
    char: Int
) {
    private val name = StringBuilder(char.toChar().toString())
}