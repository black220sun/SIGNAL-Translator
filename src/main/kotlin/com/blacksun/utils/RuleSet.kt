package com.blacksun.utils

import com.blacksun.lexer.Lexer

class RuleSet(rule: String, parts: List<String>) {
    private val rules = ArrayList<Rule>()
    private val empty: Boolean
    private val name: String
    private val type: String

    init {
        val nameParts = rule.split(' ', '\t').map(String::trim).filter(String::isNotEmpty)
        if (nameParts.size == 1) {
            type = ""
            name = nameParts[0]
        } else {
            type = nameParts[0]
            name = nameParts[1]
        }
        for (part in parts)
            rules += Rule(name, type, part)
        empty = rules.any(Rule::isEmpty)
    }

    fun isEmpty() = empty

    fun computeFirst(): ArrayList<Int> {
        val first = ArrayList<Int>()
        rules.forEach { first += it.first }
        return first
    }

    fun print() {
        if (type.isNotEmpty())
            print("$type ")
        print(name)
        print(" --> ")
        for (rule in rules.dropLast(1)) {
            rule.print()
            print("| ")
        }
        rules.last().print()
        println(";")
    }

    fun parse(): Node {
        val first = rules.mapIndexed { i, r ->  Pair(r.first, i) }
        return when (type) {
            "lexer" -> parseLexer(first)
            "part" -> parsePart(first)
            else -> parseParser(first)
        }
    }

    private fun parseParser(first: List<Pair<ArrayList<Int>, Int>>): Node {
        val token = Lexer.getToken()
        for (pair in first)
            if (token.code in pair.first || rules[pair.second].isEmpty()) {
                rules[pair.second].parse()
                val node = Node(rule=name)
                node += Node(Lexer.getToken())
                return node
            }
        error("RuleSet(Parser): no match: $token")
    }

    private fun parseLexer(first: List<Pair<ArrayList<Int>, Int>>): Node {
        val char = Lexer.read(true)
        Lexer.new()
        for (pair in first)
            if (char in pair.first || rules[pair.second].isEmpty()) {
                rules[pair.second].parse()
                val node = Node(rule=name)
                node += Node(Lexer.getToken())
                return node
            }
        error("RuleSet(Lexer): no match: $char")
    }

    private fun parsePart(first: List<Pair<ArrayList<Int>, Int>>): Node {
        val char = Lexer.read(true)
        for (pair in first)
            if (char in pair.first || rules[pair.second].isEmpty())
                return rules[pair.second].parse()
        error("RuleSet(Part): no match: $char")
    }
}
