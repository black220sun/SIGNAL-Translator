package com.blacksun

import com.blacksun.lexer.Lexer
import com.blacksun.utils.GrammarGen
import com.blacksun.utils.Node
import com.blacksun.utils.Token
import org.junit.Assert.assertEquals
import org.junit.BeforeClass
import org.junit.Test
import java.io.*

class GrammarTest {
    companion object {
        @JvmStatic
        @BeforeClass
        fun initGrammar() {
            GrammarGen.initGrammar("grammar_test")
        }
    }
    @Test
    fun testLexer() {
        val file = File("res/testLexer.sig")
        Lexer.init(file)

        val expected = Node("<identifier>")
        expected += Node(Token(1, 5, "ABC100cd"))

        val result = Lexer.createTokenNode()

        assertEquals(expected, result)
    }
    @Test
    fun testEmpty() {
        val file = File("res/testEmpty.sig")
        Lexer.init(file)

        val expected = Node("<tail>")
        expected += Node("<empty>")

        val result = GrammarGen["<tail>"].parse()

        assertEquals(expected, result)
    }
    @Test
    fun testProgram() {
        val file = File("res/testProgram.sig")
        Lexer.init(file)

        val expected = """<program>
	<program-identifiers>
		<identifier>
			1	5	ABC100cd
		<identifier>
			1	1	ABC
	<digits>
	<tail>
		<program>
			<program-identifiers>
				<identifier>
					2	1	A1B00cd
				<identifier>
			<digits>
				<unsigned-integer>
					3	1	1000
				<unsigned-integer>
					3	10	100
			<tail>
				<program>
					<program-identifiers>
						<identifier>
							3	14	a
						<identifier>
							3	16	b
					<digits>
						<unsigned-integer>
							3	18	1
						<unsigned-integer>
							3	20	2
					<tail>
						<empty>
"""

        val result = ByteArrayOutputStream()
        val old = System.out
        System.setOut(PrintStream(result))
        GrammarGen["<program>"].parse().print()
        System.setOut(old)

        assertEquals(expected, result.toString())
    }
}