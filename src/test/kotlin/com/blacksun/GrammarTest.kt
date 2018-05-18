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
        val file = File("res/testLexer.txt")
        Lexer.init(file)

        val expected = Node("<identifier>")
        expected += Node(Token(1, 5, "ABC100cd"))

        val result = Lexer.createTokenNode()

        assertEquals(expected, result)
    }
    @Test
    fun testEmpty() {
        val file = File("res/testEmpty.txt")
        Lexer.init(file)

        val expected = Node("<tail>")
        expected += Node("<empty>")

        val result = GrammarGen["<tail>"].parse()

        assertEquals(expected, result)
    }
    @Test
    fun testProgram() {
        val file = File("res/testProgram.txt")
        Lexer.init(file)

        val expected = """<program>
	<program-identifiers>
		<identifier>
			1	1	ABC
		<identifier>
			2	1	A1B00cd
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
    @Test
    fun testDelimiters() {
        val file = File("res/testDelimiters.txt")
        Lexer.init(file)

        val expected = """<test-delimiter>
	<identifier>
		1	1	ABC
	<id-with-delimiter>
		<delimiters>
			<delimiter>
				1	4	:=
			<delimiter-list>
				<empty>
		<identifier>
			1	6	A
		<id-with-delimiter>
			<delimiters>
				<delimiter>
					1	7	:=
				<delimiter-list>
					<delimiter>
						1	9	:=
					<delimiter-list>
						<empty>
			<identifier>
				1	12	ABC
			<id-with-delimiter>
				<delimiters>
					<delimiter>
						1	16	:
					<delimiter-list>
						<delimiter>
							1	17	:
						<delimiter-list>
							<delimiter>
								1	18	:
							<delimiter-list>
								<delimiter>
									1	19	:=
								<delimiter-list>
									<empty>
				<identifier>
					1	22	a1
				<id-with-delimiter>
					<empty>
"""

        val result = ByteArrayOutputStream()
        val old = System.out
        System.setOut(PrintStream(result))
        GrammarGen["<test-delimiter>"].parse().print()
        System.setOut(old)

        assertEquals(expected, result.toString())
    }
    @Test
    fun testKeyword() {
        val file = File("res/testKeyword.txt")
        Lexer.init(file)

        val expected = """<if-stmt>
	1	1	IF
	<identifier>
		1	4	a
	1	6	THEN
	<identifier>
		1	11	b
"""

        val result = ByteArrayOutputStream()
        val old = System.out
        System.setOut(PrintStream(result))
        GrammarGen["<if-stmt>"].parse().print()
        System.setOut(old)

        assertEquals(expected, result.toString())
    }
}