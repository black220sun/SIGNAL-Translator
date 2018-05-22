package com.blacksun

import com.blacksun.utils.node.Node
import com.blacksun.utils.Token
import java.io.*

object Lexer {
    private var row = 1
    private var col = 0
    var char = 0
    private lateinit var reader: Reader
    private var noRead = false
    private var token: Token? = null
    private var node: Node? = null
    private lateinit var name: String
    private var errors = 0
    private lateinit var save: Save
    private lateinit var errorMsg: String

    private data class Save(val row_: Int = row, val col_: Int = col, val char_: Int = char)

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
            token = Token()
        token!! += char
    }

    fun skip() {
        while (read() in GrammarGen["skip"].first)
            noRead = false
        hide()
    }

    fun error() {
        noRead = false
        if (char != -1) {
            ++errors
            System.err.println("Error: unexpected symbol '${char.toChar()}' at $name:$row,$col.")
        }
    }

    fun getToken(): Token {
        val tmp = token ?: error("")
        skip()
        token = null
        return tmp
    }

    fun createTokenNode(): Node {
        read()
        errorMsg = "at $name:$row,$col"
        GrammarGen.lexerRules.forEach {
            if (char in it.first) {
                node = it.parse()
                return node!!
            }
        }
        error()
        return Node()
    }

    private fun hide() {
        val tmp = if (node is Node)
            Token(node!!.token.name)
        else
            prepareToken()
        if (tmp.name() in GrammarGen["hide"].names) {
            val end = GrammarGen["show"].names[0]
            val len = tmp.name().length
            while (true) {
                read()
                tmp += char
                noRead = false
                if (char == -1) {
                    error()
                    row = save.row_
                    col = save.col_
                    ++errors
                    System.err.println("Unclosed comment at $name:$row,$col.")
                    break
                }
                if (tmp.name().drop(len).endsWith(end))
                    break
            }
        } else if (node == null) {
            reader.reset()
            noRead = true
            row = save.row_
            col = save.col_
            char = save.char_
        }
    }

    private fun prepareToken(): Token {
        val name = GrammarGen["hide"].names[0]
        val len = name.length
        val tmp = Token()
        reader.mark(len)
        save = Save()
        for (i in 1..len) {
            read()
            tmp += char
            noRead = false
            if (!name.startsWith(tmp.name()))
                break
        }
        return tmp
    }

    fun getTokenNode(): Node? {
        val tmp = node
        node = null
        return tmp
    }

    fun getErrors(): Int {
        if (char != -1) {
            ++errors
            System.err.println("Unparsed symbols left for $name")
        }
        return errors
    }

    fun errorMsg() = errorMsg
}