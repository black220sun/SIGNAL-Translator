package com.blacksun.lexer

import com.blacksun.utils.Token
import java.io.File
import java.io.FileReader

class Lexer(file: File) {
    private var row = 1
    private var col = 0
    private var char = 0
    private val reader = FileReader(file)

    constructor(path: String) : this(File(path))

    fun getToken(): Token {
        return Token(col, row, char)
    }

    private fun read() {
        char = reader.read()
    }
}