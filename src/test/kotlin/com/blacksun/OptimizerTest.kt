package com.blacksun

import com.blacksun.optimizer.OptimizeEmpty
import com.blacksun.optimizer.Optimizer
import org.junit.BeforeClass
import org.junit.Test
import kotlin.test.assertEquals

class OptimizerTest {
    companion object {
        @JvmStatic
        @BeforeClass
        fun initGrammar() {
            GrammarGen.initGrammar("grammar.gr")
        }
    }
    @Test
    fun testEmpty() {
        val expected = GrammarGen.parse("""
            PROGRAM EMPTY;
            BEGIN
            END.
        """.trimIndent())
        val start = GrammarGen.parse("""
            PROGRAM EMPTY;
            BEGIN
            ;;;;;;;;;;;;;;;;;;;;
            END.
        """.trimIndent())
        val optimizer = Optimizer() + OptimizeEmpty()

        val result = optimizer.process(start)

        assertEquals(expected, result)
    }
    @Test
    fun testEmpty2() {
        val expected = GrammarGen.parse("""
            PROGRAM EMPTY;
            BEGIN
            IF A = B THEN ENDIF;
            END.
        """.trimIndent())
        val start = GrammarGen.parse("""
            PROGRAM EMPTY;
            BEGIN
            ;;;;;;;IF A=B THEN;;;;;ENDIF;;;;;;
            END.
        """.trimIndent())
        val optimizer = Optimizer() + OptimizeEmpty()

        val result = optimizer.process(start)

        assertEquals(expected, result)
    }

    @Test
    fun testEmpty3() {
        val expected = GrammarGen.parse("""
            PROGRAM EMPTY;
            BEGIN
            END.
        """.trimIndent())
        val start = GrammarGen.parse("""
            PROGRAM EMPTY;
            BEGIN
            ;;;;;;;LOOP;;ENDLOOP;;;;;
            WHILE A = 10 * 12 DO ;; ENDWHILE;
            END.
        """.trimIndent())
        val optimizer = Optimizer() + OptimizeEmpty()

        val result = optimizer.process(start)

        assertEquals(expected, result)
    }
}