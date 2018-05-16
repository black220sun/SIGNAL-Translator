package com.blacksun.utils.rule

import com.blacksun.utils.GrammarGen

class PartRule(name: String) : Rule(name) {
    override val parse = { GrammarGen[name].parse() }

    override val computeFirst = {
        GrammarGen[name].first
    }
}
