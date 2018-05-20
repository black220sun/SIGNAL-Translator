package com.blacksun.lexer

import com.blacksun.utils.GrammarGen
import com.blacksun.utils.Node
import com.blacksun.utils.Token
import java.io.*

object Lexer {
    private var row = 1
    private var col = 0
    private var char = 0
    private lateinit var reader: Reader
    private var noRead = false
    private var token: Token? = null
    private var node: Node? = null
    private lateinit var name: String
    private var errors = 0

    fun init(text: String) {
        name = "input from string"
        reader = StringReader(text)
        init()
    }

    fun init(file: File) {
        name = file.name
        reader = FileReader(file)
        init()
    }

    private fun init() {
        reader = BufferedReader(reader)
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
        hide()
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

    private fun hide() {
        val tmp = if (node is Node)
            Token(row, col, node!!.token.name)
        else
            prepareToken()
        if (tmp.name() in GrammarGen["hide"].names) {
            val end = GrammarGen["show"].names[0]
            val len = tmp.name().length
            while (true) {
                read()
                tmp += char
                noRead = false
                val name = tmp.name()
                if (name.drop(len).endsWith(end)) {
                    node = null
                    token = null
                    createTokenNode()
                    break
                }
            }
        } else if (node == null) {
            reader.reset()
        }
    }

    private fun prepareToken(): Token {
        val len = GrammarGen["hide"].names[0].length
        val tmp = Token(row, col)
        reader.mark(len)
        for (i in 1..len) {
            tmp += char
            read()
        }
        return tmp
    }

    fun getTokenNode(): Node? {
        val tmp = node
        node = null
        return tmp
    }

    fun getErrors() = errors
}