package com.blacksun.utils

import com.blacksun.utils.rule.RuleSet
import java.io.File
import java.io.FileReader

object GrammarGen {
    private val map = HashMap<String, RuleSet>()
    operator fun get(name: String) = map[name]!!
    private var rule = StringBuilder()
    lateinit var first: RuleSet

    fun initGrammar(path: String) = initGrammar(File(path))

    fun initGrammar(file: File) {
        FileReader(file).forEachLine(::parseLine)
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
        val name = parts[0]
        map[name] = RuleSet(name, parts[1])
        if (!::first.isInitialized)
            first = map[name]!!
    }

    fun print() = map.forEach { _, r -> println(r) }
}