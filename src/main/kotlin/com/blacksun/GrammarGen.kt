package com.blacksun

import com.blacksun.utils.node.Node
import com.blacksun.utils.rule.RuleSet
import java.io.File
import java.io.FileReader

object GrammarGen {
    private val map = LinkedHashMap<String, RuleSet>()
    operator fun get(name: String) = map[name]!!
    private var rule = StringBuilder()
    private lateinit var first: String
    val lexerRules by lazy { map.map { (_, r) -> r }.filter { it.type == "lexer" } }
    private val keywords_ = HashMap<String, Boolean>()
    private val keywords by lazy { keywords_.keys }
    private var errors = 0

    fun initGrammar(path: String) = initGrammar(File(path))

    fun initGrammar(file: File) {
        keywords_.clear()
        map.clear()
        FileReader(file).forEachLine(GrammarGen::parseLine)
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
        if (!GrammarGen::first.isInitialized)
            first = name
    }

    private fun parseParts(part: String): Pair<String, String> {
        val name = part.split(' ', '\t').last()
        val type = part.removeSuffix(name).trimEnd()
        return Pair(name, type)
    }

    fun print() = map.forEach { _, r -> println(r) }

    fun parse(text: String, rule: String = first): Node {
        Lexer.init(text)
        return parse("Input from string", rule, 1)
    }

    fun parse(file: File, rule: String = first): Node {
        Lexer.init(file)
        return parse(file.name, rule, 1)
    }

    private fun parse(name: String, rule: String, @Suppress("UNUSED_PARAMETER") void: Any): Node {
        val startRule = if (rule.isBlank()) first else rule
        errors = 0
        val node = GrammarGen[startRule].parse()
        val lexerErrors = Lexer.getErrors()
        if (errors > 0 || lexerErrors > 0)
            println("$name parsed with errors: $lexerErrors lexer errors, $errors parser errors")
        return node
    }

    fun isKeyword(name: String): Boolean = name in keywords
    fun registerKeyword(name: String) = keywords_.put(name, true)
    fun error() = ++errors
}