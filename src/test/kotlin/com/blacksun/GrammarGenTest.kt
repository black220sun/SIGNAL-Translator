package com.blacksun

import com.blacksun.utils.GrammarGen
import org.junit.Test
import java.io.File
import java.io.FileReader
import java.io.PrintStream
import kotlin.test.assertEquals

class GrammarGenTest {
    private val grammarFile = "grammar_test"
    @Test
    fun testInitGrammar() {
        val grammarFile = File(grammarFile)
        GrammarGen.initGrammar(grammarFile)
        val testFile = File.createTempFile("test", ".tmp")
        val stream = PrintStream(testFile)
        System.setOut(stream)
        GrammarGen.print()
        stream.close()

        val expected  = FileReader(grammarFile).readLines()
        val result = FileReader(testFile).readLines()

        assertEquals(expected, result)
    }

    @Test
    fun testComputeFirst() {
        GrammarGen.initGrammar(grammarFile)

        val expected = GrammarGen["letter"].first + GrammarGen["digit"].first
        val result = GrammarGen["string"].first

        assertEquals(expected, result)
        assert(GrammarGen["string"].isEmpty())
        assert(!GrammarGen["letter"].isEmpty())
    }
}
