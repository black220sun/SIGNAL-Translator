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

    private data class Save(
            val row_: Int = row, val col_: Int = col,
            val char_: Int = char, val err: Int = errors)

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
        if (noRead) {
            Logger.info("Reading char. noRead = true, return $char [${char.toChar()}]")
            return char
        }
        noRead = true
        char = reader.read()
        Logger.info("Read new char: $char [${char.toChar()}]")
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
            Logger.err("Error: unexpected symbol '${char.toChar()}' at $name:$row,$col.")
        }
    }

    fun getToken(): Token {
        val tmp = token ?: error("")
        skip()
        token = null
        Logger.info("Return $tmp")
        return tmp
    }

    fun createTokenNode(): Node {
        Logger.info("Creating tokenNode")
        read()
        errorMsg = "at $name:$row,$col"
        GrammarGen.lexerRules.forEach {
            if (char in it.first) {
                Logger.info("Char matched, parsing $it")
                node = it.parse()
                return node!!
            }
        }
        error()
        return Node()
    }

    private fun hide() {
        Logger.info("Skipping comments")
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
                    Logger.err("Unclosed comment at $name:$row,$col.")
                    break
                }
                if (tmp.name().drop(len).endsWith(end))
                    break
            }
        } else if (node == null) {
            rollback()
        }
    }

    private fun prepareToken(): Token {
        Logger.info("Preparing token")
        val name = GrammarGen["hide"].names[0]
        val len = name.length
        val tmp = Token()
        savepoint(len)
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
            Logger.err("Unparsed symbols left for $name")
        }
        return errors
    }

    fun savepoint(length: Int) {
        Logger.info("Creating savepoint for $length chars")
        reader.mark(length)
        save = Save()
    }

    fun rollback() {
        Logger.info("Rollback to savepoint")
        noRead = true
        reader.reset()
        row = save.row_
        col = save.col_
        char = save.char_
        errors = save.err
    }

    fun errorMsg() = errorMsg
}