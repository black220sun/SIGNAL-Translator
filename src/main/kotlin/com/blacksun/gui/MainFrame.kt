package com.blacksun.gui

import com.blacksun.gui.util.SFileChooser
import com.blacksun.optimizer.OptimizeEmpty
import com.blacksun.optimizer.Optimizer
import com.blacksun.settings.Settings
import com.blacksun.utils.GrammarGen
import java.awt.Dimension
import java.awt.event.WindowEvent
import java.awt.event.WindowListener
import java.io.ByteArrayOutputStream
import java.io.FileReader
import javax.swing.JFileChooser
import javax.swing.JFrame
import javax.swing.JOptionPane
import javax.swing.JSplitPane
import java.io.File
import java.io.PrintStream

object MainFrame: JFrame(), WindowListener {
    private val inputPanel = InputPanel()
    private val outputPanel = OutputPanel()
    private val toolbar = Toolbar()

    init {
        defaultCloseOperation = DO_NOTHING_ON_CLOSE
        addWindowListener(this)
        preferredSize = Dimension(1200, 800)
        val panel = JSplitPane(JSplitPane.HORIZONTAL_SPLIT, inputPanel, outputPanel)
        panel.isOneTouchExpandable = true
        contentPane = JSplitPane(JSplitPane.VERTICAL_SPLIT, toolbar, panel)
        isVisible = true
        pack()
        Settings.setProperty("grammarPath", File("grammar.gr").absolutePath)
        GrammarGen.initGrammar(Settings.getProperty("grammarPath")!!)
    }

    override fun windowDeiconified(p0: WindowEvent?) = Unit
    override fun windowClosed(p0: WindowEvent?) = Unit
    override fun windowActivated(p0: WindowEvent?) = Unit
    override fun windowDeactivated(p0: WindowEvent?) = Unit
    override fun windowOpened(p0: WindowEvent?) = Unit
    override fun windowIconified(p0: WindowEvent?) = Unit
    override fun windowClosing(p0: WindowEvent?) = close()


    private fun close() {
        if (Settings.getForce("forceQuit")) {
            quit()
            return
        }
        val result = JOptionPane.showConfirmDialog(this, Settings.getLang("Quit?"), "", JOptionPane.YES_NO_OPTION)
        if (result == JOptionPane.YES_OPTION) {
            quit()
        }
    }

    private fun quit() {
        Settings.save()
        Settings.saveLang()
        dispose()
    }

    fun openFile() {
        val fileChooser = SFileChooser()
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            val file = fileChooser.getFile()
            inputPanel.textArea.text = FileReader(file).readText()
        }
    }

    fun loadGrammar() {
        val fileChooser = SFileChooser()
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            val path = fileChooser.getFile().absolutePath
            Settings.setProperty("grammarPath", path)
            GrammarGen.initGrammar(path)
        }
    }

    fun process() {
        val rule = toolbar.getRule()
        val node = GrammarGen.parse(inputPanel.textArea.text, rule)

        val result = ByteArrayOutputStream()
        val old = System.out
        System.setOut(PrintStream(result))

        node.print("--")
        outputPanel.textArea.text = result.toString()

        System.setOut(old)
    }

    fun optimize() {
        val rule = toolbar.getRule()
        val node = GrammarGen.parse(inputPanel.textArea.text, rule)
        val optimized = (Optimizer() + OptimizeEmpty()).process(node)

        val result = ByteArrayOutputStream()
        val old = System.out
        System.setOut(PrintStream(result))

        optimized.print("--")
        outputPanel.textArea.text = result.toString()

        System.setOut(old)
    }
}