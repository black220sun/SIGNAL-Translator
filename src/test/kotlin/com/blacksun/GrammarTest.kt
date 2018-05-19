package com.blacksun

import com.blacksun.lexer.Lexer
import com.blacksun.utils.GrammarGen
import com.blacksun.utils.node.Node
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
        expected += Node(Token("ABC100cd"))

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
			ABC
		<identifier>
			A1B00cd
	<digits>
		<unsigned-integer>
			1000
		<unsigned-integer>
			100
	<tail>
		<program>
			<program-identifiers>
				<identifier>
					a
				<identifier>
					b
			<digits>
				<unsigned-integer>
					1
				<unsigned-integer>
					2
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
		ABC
	<id-with-delimiter>
		<delimiters>
			<delimiter>
				:=
			<delimiter-list>
				<empty>
		<identifier>
			A
		<id-with-delimiter>
			<delimiters>
				<delimiter>
					:=
				<delimiter-list>
					<delimiter>
						:=
					<delimiter-list>
						<empty>
			<identifier>
				ABC
			<id-with-delimiter>
				<delimiters>
					<delimiter>
						:
					<delimiter-list>
						<delimiter>
							:
						<delimiter-list>
							<delimiter>
								:
							<delimiter-list>
								<delimiter>
									:=
								<delimiter-list>
									<empty>
				<identifier>
					a1
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
	IF
	<identifier>
		a
	THEN
	<identifier>
		b
"""

        val result = ByteArrayOutputStream()
        val old = System.out
        System.setOut(PrintStream(result))
        GrammarGen["<if-stmt>"].parse().print()
        System.setOut(old)

        assertEquals(expected, result.toString())
    }
}