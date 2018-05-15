package com.blacksun.lexer

import com.blacksun.utils.Token
import java.io.File
import java.io.FileReader

object Lexer {
    private var row = 1
    private var col = 0
    private var char = 0
    private lateinit var reader: FileReader
    private var noRead = false
    private lateinit var token: Token

    fun init(path: String) = init(File(path))

    fun init(file: File) {
        reader = FileReader(file)
    }

    fun getToken(): Token = token

    fun read(save: Boolean = false): Int {
        if (noRead) {
            noRead = save
            return char
        }
        noRead = save
        char = reader.read()
        if (char == '\n'.toInt()) {
            ++row
            col = 0
        } else
            ++col
        return char
    }

    fun new() {
        token = Token(row, col)
    }

    fun add() {
        token += char
        noRead = false
    }
}