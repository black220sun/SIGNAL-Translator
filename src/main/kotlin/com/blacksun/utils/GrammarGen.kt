package com.blacksun.utils

import java.io.File
import java.io.FileReader

object GrammarGen {
    private val map = LinkedHashMap<String, RuleSet>()
    private var rule = StringBuilder()

    fun initGrammar(path: String) = initGrammar(File(path))

    fun initGrammar(file: File) {
        map.clear()
        FileReader(file).forEachLine(::parse)
    }

    private fun parse(line: String) {
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
        map[name] = RuleSet(name, parts[1].split("|").map(String::trim).filter(String::isNotEmpty))
    }

    fun print() {
        map.forEach { _, ruleSet ->  ruleSet.print() }
    }
}
