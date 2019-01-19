package com.blacksun.utils.rule

import com.blacksun.Lexer
import com.blacksun.GrammarGen
import com.blacksun.Logger
import com.blacksun.utils.node.Node

class RuleSet(private val name: String, val type: String, parts: String) {
    private val rules = ArrayList<RuleAlternative>()
    val first by lazy(::computeFirst)
    val names by lazy(::computeNames)
    val parse: () -> Node
    val empty by lazy { rules.any { it.empty } }

    init {
        when (type) {
            "part" -> parse = {
                Logger.info("Parsing [part] $name")
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
                Logger.info("Parsing [lexer] $name")
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
                Logger.info("Parsing $name")
                val token = Lexer.createTokenNode()
                val node = Node(name)
                var flag = true
                for (rule in rules)
                    if (rule.check(token)) {
                        val result = if (rule.rollback) {
                            val err = GrammarGen.savepoint(1000)
                            val resultOrError = rule.parse()
                            if (GrammarGen.getErrors() != err) {
                                GrammarGen.rollback(err, token)
                                continue
                            }
                            resultOrError
                        } else {
                            rule.parse()
                        }
                        node += result
                        flag = false
                        break
                    }
                if (flag)
                    error(token)
                node
            }
        }
        Logger.info("Creating RuleSet $name" + if (type.isBlank()) "" else " [$type]")
        for (part in parts.split('|'))
            rules += RuleAlternative(part.trim())
    }

    private fun error(node: Node?) {
        if (node is Node) {
            GrammarGen.error()
            var tokenName = node.token.name
            if (tokenName.isBlank())
                tokenName = Lexer.char.toChar().toString()
            Logger.err("Error ${Lexer.errorMsg()}: expected $name, found $tokenName")
        } else
            Lexer.error()
    }

    private fun computeFirst(): ArrayList<Int> {
        val first = ArrayList<Int>()
        for (alternative in rules) {
            first += alternative.first
            // if any pair or alternatives has the same first token (not LL(1)) -
            // mark as rollback-needed
            for (rule in rules) {
                if (rule == alternative)
                    continue
                if (rule.first.any { it in alternative.first }) {
                    Logger.info("Mark $rule and $alternative as rollback")
                    rule.rollback = true
                    alternative.rollback = true
                }
            }
        }
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