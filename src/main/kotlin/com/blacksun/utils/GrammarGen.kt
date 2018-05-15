package com.blacksun.utils

import com.blacksun.lexer.Lexer
import java.io.File
import java.io.FileReader

object GrammarGen {
    private val map = LinkedHashMap<String, RuleSet>()
    private var rule = StringBuilder()
    private lateinit var first: String

    fun initGrammar(path: String) = initGrammar(File(path))

    fun initGrammar(file: File) {
        map.clear()
        FileReader(file).forEachLine(::parseLine)
        map.forEach { _, ruleSet ->  ruleSet.computeFirst() }
    }

    fun parse(file: File): Node {
        Lexer.init(file)
        return map[first]!!.parse()
    }

    fun parse(path: String) = parse(File(path))

    operator fun get(name: String) = map[name]!!

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
        if (!::first.isInitialized)
            first = name
        map[name] = RuleSet(left, parts[1].split("|").map(String::trim))
    }

    fun print() {
        map.forEach { _, ruleSet ->  ruleSet.print() }
    }
}
