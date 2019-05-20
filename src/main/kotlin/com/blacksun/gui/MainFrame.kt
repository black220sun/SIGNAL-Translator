package com.blacksun.gui

import com.blacksun.gui.util.SFileChooser
import com.blacksun.optimizer.Optimizer
import com.blacksun.settings.Settings
import com.blacksun.GrammarGen
import com.blacksun.gui.util.LFrame
import com.blacksun.optimizer.Optimization
import com.blacksun.utils.node.Node
import java.awt.Dimension
import java.awt.event.WindowEvent
import java.awt.event.WindowListener
import java.io.*
import javax.swing.JFileChooser
import javax.swing.JOptionPane
import javax.swing.JSplitPane

object MainFrame: LFrame("SIGNAL Translator"), WindowListener {
    private val inputPanel = InputPanel()
    private val panel = JSplitPane(JSplitPane.HORIZONTAL_SPLIT, inputPanel, OutputPanel())
    private val toolbar = Toolbar()
    private var root: Node? = null
    private val out = ByteArrayOutputStream()
    private val err = ByteArrayOutputStream()
    private var file: File? = {
        val tmp = Settings.getProperty("activeFile")
        if (tmp != null) {
            val file = File(tmp)
            inputPanel.textArea.text = FileReader(file).readText()
            file
        } else null
    }()

    init {
        System.setOut(PrintStream(out))
        System.setErr(PrintStream(err))
        defaultCloseOperation = DO_NOTHING_ON_CLOSE
        addWindowListener(this)
        preferredSize = Dimension(1200, 800)
        panel.preferredSize = Dimension(1200, 700)
        panel.resetToPreferredSizes()
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
        if (file != null)
            Settings.setProperty("activeFile", file!!.absolutePath)
        Settings.save()
        Settings.saveLang()
        dispose()
        System.exit(0)
    }

    fun openFile() {
        val fileChooser = SFileChooser()
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getFile()
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
        root = GrammarGen.parse(inputPanel.textArea.text, rule)
        error()
        panel.rightComponent = if (Settings.getForce("forcePrint")) {
            root!!.print("--")
            val panel = OutputPanel(out.toString())
            out.reset()
            panel
        } else
            TreePanel(root!!)
    }

    fun optimize() {
        val rule = toolbar.getRule()
        val node = root ?: GrammarGen.parse(inputPanel.textArea.text, rule)
        val optimizer = Optimizer()
        val optimizationNames = Settings.getProperties("forceOptimize.+")
        optimizationNames.forEach {
            optimizer += Class.forName("com.blacksun.optimizer." + it.drop(5)).newInstance() as Optimization
        }
        root = optimizer.process(node)
        root = optimizer.process(root!!)
        error()
        panel.rightComponent = if (Settings.getForce("forcePrint")) {
            root!!.print("--")
            val panel = OutputPanel(out.toString())
            out.reset()
            panel
        } else
            TreePanel(root!!)
        root = null
    }

    private fun error() {
        val errors = err.toString()
        if (errors.isNotEmpty()) {
            err.reset()
            ErrorFrame(errors)
        }
    }

    fun save() {
        if (file == null) {
            val fileChooser = SFileChooser()
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                file = fileChooser.getFile()
            } else
                return
        }
        val writer = FileWriter(file)
        writer.write(inputPanel.textArea.text)
        writer.close()
    }

    fun closeFile() {
        file = null
        inputPanel.textArea.text = ""
    }
}