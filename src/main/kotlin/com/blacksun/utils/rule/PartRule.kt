package com.blacksun.utils.rule

import com.blacksun.utils.GrammarGen
import com.blacksun.utils.Node

class PartRule(name: String) : Rule(name) {
    override val empty by lazy { GrammarGen[name].empty }
    override val parse = { GrammarGen[name].parse() }
    override val computeFirst = { GrammarGen[name].first }
    override val computeNames = { GrammarGen[name].names + name }
    override fun check(value: Any): Boolean =
            if (value is Node)
                value.token.name in names || value.value == name || empty
            else
                value in first
}
