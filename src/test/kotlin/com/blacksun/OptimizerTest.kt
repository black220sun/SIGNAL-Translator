package com.blacksun

import com.blacksun.optimizer.*
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
        val optimizer = Optimizer() + OptimizeEmpty() + OptimizeEmptyLoop() + OptimizeEmptyWhile()

        val result = optimizer.process(start)

        assertEquals(expected, result)
    }
    @Test
    fun testEmptyElse() {
        val expected = GrammarGen.parse("""
            PROGRAM EMPTY;
            BEGIN
            IF A = B THEN B := 10; ENDIF;
            END.
        """.trimIndent())
        val start = GrammarGen.parse("""
            PROGRAM EMPTY;
            BEGIN
            ;;;IF A = B THEN B := 10;;;; ELSE ;;;;; ENDIF;
            END.
        """.trimIndent())
        val optimizer = Optimizer() + OptimizeEmpty() + OptimizeEmptyElse()

        val result = optimizer.process(start)

        assertEquals(expected, result)
    }
    @Test
    fun testEmptyThen() {
        val expected = GrammarGen.parse("""
            PROGRAM EMPTY;
            BEGIN
            IF NOT [A = B] THEN B := 10; ENDIF;
            END.
        """.trimIndent())
        val start = GrammarGen.parse("""
            PROGRAM EMPTY;
            BEGIN
            IF A = B THEN ELSE B := 10; ENDIF;
            END.
        """.trimIndent())
        val optimizer = Optimizer() + OptimizeEmptyThen()

        val result = optimizer.process(start)

        assertEquals(expected, result)
    }
    @Test
    fun testEmptyIf() {
        val expected = GrammarGen.parse("""
            PROGRAM EMPTY;
            BEGIN
            END.
        """.trimIndent())
        val start = GrammarGen.parse("""
            PROGRAM EMPTY;
            BEGIN
            IF NOT [A = B] THEN ELSE ENDIF;
            IF A = B THEN ENDIF;
            END.
        """.trimIndent())
        val optimizer = Optimizer() + OptimizeEmptyThen() + OptimizeEmptyIf()

        val result = optimizer.process(start)

        assertEquals(expected, result)
    }
    @Test
    fun testEmptyAlternative() {
        val expected = GrammarGen.parse("""
            PROGRAM EMPTY;
            BEGIN
            CASE 10 OF
            11: /B:=1;\
            13: /C:=2;\
            ENDCASE;
            END.
        """.trimIndent())
        val start = GrammarGen.parse("""
            PROGRAM EMPTY;
            BEGIN
            CASE 10 OF
            10: /\
            11: /B:=1;\
            12: /\
            13: /C:=2;\
            ENDCASE;
            END.
        """.trimIndent())
        val optimizer = Optimizer() + OptimizeEmptyAlternative()

        val result = optimizer.process(start)

        assertEquals(expected, result)
    }
    @Test
    fun testEmptyCase() {
        val expected = GrammarGen.parse("""
            PROGRAM EMPTY;
            BEGIN
            CASE 10 OF
            11: /B:=1;\
            13: /C:=2;\
            ENDCASE;
            END.
        """.trimIndent())
        val start = GrammarGen.parse("""
            PROGRAM EMPTY;
            BEGIN
            CASE 1 OF
            10: /\
            11: /;;;;\
            12: /\
            13: /;\
            ENDCASE;

            CASE 10 OF
            10: /\
            11: /B:=1;\
            12: /\
            13: /C:=2;\
            ENDCASE;
            END.
        """.trimIndent())
        val optimizer = Optimizer() + OptimizeEmpty() + OptimizeEmptyAlternative() + OptimizeEmptyCase()

        val result = optimizer.process(start)

        assertEquals(expected, result)
    }
}