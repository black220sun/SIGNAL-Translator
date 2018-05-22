package com.blacksun.optimizer

import com.blacksun.utils.node.MatcherRules
import com.blacksun.utils.node.Node

class Optimizer {
    private val rules by lazy { computeRules() }
    private val optimizations = HashSet<Optimization>()

    operator fun plus(kind: Optimization): Optimizer {
        optimizations += kind
        return this
    }

    operator fun plusAssign(kind: Optimization) = optimizations.plusAssign(kind)

    fun process(node: Node): Node = node.rewrite(rules)

    private fun computeRules(): MatcherRules {
        val rules = MatcherRules()
        optimizations.forEach {
            val opt = it.optimization()
            rules[it.name]?.plusAssign(opt) ?: rules.put(it.name, arrayListOf(opt))
        }
        return rules
    }
}