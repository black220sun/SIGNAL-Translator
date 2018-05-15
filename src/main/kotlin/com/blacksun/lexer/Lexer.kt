package com.blacksun.lexer

import com.blacksun.utils.RuleSet
import com.blacksun.utils.Token
import java.io.File
import java.io.FileReader

object Lexer {
    private val map = HashMap<String, RuleSet>()
    private var rule = StringBuilder()
    private var row = 1
    private var col = 0
    private var char = 0
    private lateinit var reader: FileReader
    private var noRead = false
    private lateinit var token: Token

    operator fun get(name: String) = map[name]!!

    fun initGrammar(path: String) = initGrammar(File(path))

    fun initGrammar(file: File) {
        map.clear()
        FileReader(file).forEachLine(::parseLine)
        map.forEach { _, ruleSet ->  ruleSet.first }
    }

    private fun parseLine(line: String) {
        if (line.endsWith(";")) {
            rule.append(line.dropLast(1))
            parseRule()
        } else
            rule.append(line)
    }

    private fun parseRule() {
        val parts = rule.split("-->").map(String::trim)
        rule = StringBuilder()
        val left = parts[0]
        val name = left.split(' ', '\t').last()
        map[name] = RuleSet(left, parts[1].split("|").map(String::trim))
    }

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