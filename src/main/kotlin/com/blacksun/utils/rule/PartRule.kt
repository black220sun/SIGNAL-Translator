package com.blacksun.utils.rule

import com.blacksun.utils.GrammarGen

class PartRule(name: String) : Rule(name) {
    override val empty by lazy { GrammarGen[name].empty }
    override val parse = { GrammarGen[name].parse() }
    override val computeFirst = { GrammarGen[name].first }
    override val computeNames = { GrammarGen[name].names + name }
}
