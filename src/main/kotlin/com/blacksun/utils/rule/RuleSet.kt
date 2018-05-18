package com.blacksun.utils.rule

import com.blacksun.lexer.Lexer
import com.blacksun.utils.GrammarGen
import com.blacksun.utils.Node

class RuleSet(private val name: String, val type: String, parts: String) {
    private val rules = ArrayList<RuleAlternative>()
    val first by lazy(::computeFirst)
    val names by lazy(::computeNames)
    val parse: () -> Node
    val empty by lazy { rules.any { it.empty } }

    init {
        when (type) {
            "part" -> parse = {
                val char = Lexer.read()
                var flag = true
                for (rule in rules)
                    if (rule.check(char)) {
                        rule.parse()
                        flag = false
                        break
                    }
                if (flag)
                    Lexer.error()
                Node()
            }
            "lexer" -> parse = {
                val tokenNode = Lexer.getTokenNode()
                if (tokenNode is Node)
                    tokenNode
                else {
                    val char = Lexer.read()
                    val node = Node(name)
                    var flag = true
                    for (rule in rules)
                        if (rule.check(char)) {
                            rule.parse()
                            flag = false
                            node += Node(Lexer.getToken())
                            break
                        }
                    if (flag)
                        error(tokenNode)
                    node
                }
            }
            else -> parse = {
                val token = Lexer.createTokenNode()
                val node = Node(name)
                var flag = true
                for (rule in rules)
                    if (rule.check(token)) {
                        flag = false
                        node += rule.parse()
                        break
                    }
                if (flag)
                    error(token)
                node
            }
        }
        for (part in parts.split('|'))
            rules += RuleAlternative(part.trim())
    }

    private fun error(node: Node?) {
        if (node is Node) {
            GrammarGen.error()
            println("Error: expected $name, found ${node.token.name}")
        } else
            Lexer.error()
    }

    private fun computeFirst(): ArrayList<Int> {
        val first = ArrayList<Int>()
        for (rule in rules)
            first += rule.first
        return first
    }

    private fun computeNames(): ArrayList<String> {
        val names = ArrayList<String>()
        for (rule in rules)
            names += rule.names
        return names
    }

    override fun toString() = "$name --> " + rules.joinToString(" | ") + ';'
}