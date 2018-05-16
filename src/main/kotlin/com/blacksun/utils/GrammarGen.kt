package com.blacksun.utils

import com.blacksun.utils.rule.RuleSet
import java.io.File
import java.io.FileReader

object GrammarGen {
    private val map = HashMap<String, RuleSet>()
    operator fun get(name: String) = map[name]!!
    private var rule = StringBuilder()
    lateinit var first: RuleSet
    val lexerRules by lazy { map.map { (_, r) -> r }.filter { it.type == "lexer" } }

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
        val (name, type) = parseParts(parts[0])
        map[name] = RuleSet(name, type, parts[1])
        if (!::first.isInitialized)
            first = map[name]!!
    }

    private fun parseParts(part: String): Pair<String, String> {
        val name = part.split(' ', '\t').last()
        val type = part.removeSuffix(name).trimEnd()
        return Pair(name, type)
    }

    fun print() = map.forEach { _, r -> println(r) }
}