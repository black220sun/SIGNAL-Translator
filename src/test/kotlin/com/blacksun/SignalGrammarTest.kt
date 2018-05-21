package com.blacksun

import org.junit.Assert
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream

class SignalGrammarTest {
    @Test
    fun testAll() {
        val result = ByteArrayOutputStream()
        val old = System.out
        System.setOut(PrintStream(result))
        GrammarGen.initGrammar("grammar.gr")

        File("res").listFiles{ _, s -> s.endsWith(".sig") }.forEach {
            try {
                GrammarGen.parse(it)
            } catch (e: Exception) {
                println(it.name)
            }
        }

        System.setOut(old)

        Assert.assertEquals("", result.toString())
    }
}