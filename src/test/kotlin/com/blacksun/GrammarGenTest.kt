package com.blacksun

import com.blacksun.utils.GrammarGen
import org.junit.Test
import java.io.File
import java.io.FileReader
import java.io.PrintStream
import kotlin.test.assertEquals

class GrammarGenTest {
    @Test
    fun testInitGrammar() {
        val grammarFile = File("grammar")
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
}
