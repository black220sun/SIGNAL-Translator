package com.blacksun.utils.rule

import com.blacksun.GrammarGen
import com.blacksun.utils.node.Node

class PartRule(name: String) : Rule(name) {
    override val empty by lazy { GrammarGen[name].empty }
    override val parse = { GrammarGen[name].parse() }
    override val computeFirst = { GrammarGen[name].first }
    override val computeNames = { GrammarGen[name].names + name }
    override fun check(value: Any): Boolean =
            if (value is Node) {
                val pre = empty || value.token.name in names
                if (pre)
                    true
                else
                    !GrammarGen.isKeyword(value.token.name) && value.value in names
            } else
                value in first
}
