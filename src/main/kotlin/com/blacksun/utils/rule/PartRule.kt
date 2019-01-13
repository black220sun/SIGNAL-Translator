package com.blacksun.utils.rule

import com.blacksun.GrammarGen
import com.blacksun.Logger
import com.blacksun.utils.node.Node

class PartRule(name: String) : Rule(name) {
    override val empty by lazy { GrammarGen[name].empty }
    override val parse = {
        Logger.info("Parsing PartRule $name")
        val node = GrammarGen[name].parse()
        Logger.info("$name parsed")
        node
    }
    override val computeFirst = {
        val first = GrammarGen[name].first
        Logger.info("$name first computed as $first")
        first
    }
    override val computeNames = {
        val names = GrammarGen[name].names + name
        Logger.info("$name names computed as $names")
        names
    }
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
