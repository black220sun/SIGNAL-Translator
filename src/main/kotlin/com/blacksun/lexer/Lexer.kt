package com.blacksun.lexer

import com.blacksun.utils.GrammarGen
import com.blacksun.utils.Node
import com.blacksun.utils.Token
import java.io.File
import java.io.FileReader

object Lexer {
    private var row = 1
    private var col = 0
    private var char = 0
    private lateinit var reader: FileReader
    private var noRead = false
    private var token: Token? = null
    private var node: Node? = null
    private lateinit var name: String
    private var errors = 0

    fun init(path: String) = init(File(path))

    fun init(file: File) {
        reader = FileReader(file)
        name = file.name
        row = 1
        col = 0
        noRead = false
        char = -1
        node = null
        errors = 0
        skip()
    }

    fun read(): Int {
        if (noRead)
            return char
        noRead = true
        char = reader.read()
        if (char == '\n'.toInt()) {
            ++row
            col = 0
        } else
            ++col
        return char
    }

    fun add() {
        noRead = false
        if (token == null)
            token = Token(row, col)
        token!! += char
    }

    fun skip() {
        while (read() in GrammarGen["skip"].first)
            noRead = false
    }

    fun error() {
        ++errors
        println("Error: unexpected symbol '${char.toChar()}' at $name:$row,$col.")
    }

    fun getToken(): Token {
        val tmp = token ?: error("")
        skip()
        token = null
        return tmp
    }

    fun createTokenNode(): Node {
        read()
        GrammarGen.lexerRules.forEach {
            if (char in it.first) {
                node = it.parse()
                return node!!
            }
        }
        return Node("error")
    }

    fun getTokenNode(): Node? {
        val tmp = node
        node = null
        return tmp
    }

    fun getErrors() = errors
}