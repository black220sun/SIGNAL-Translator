package com.blacksun.gui

import com.blacksun.gui.util.LCheckBox
import com.blacksun.gui.util.LIcon
import com.blacksun.settings.Settings
import java.awt.Dimension
import javax.swing.BoxLayout
import javax.swing.JPanel
import javax.swing.JTextField

class Toolbar: JPanel() {
    private val rule = JTextField()
    private val tree = LCheckBox("Printed view", Settings.getForce("forcePrint"))
    init {
        layout = BoxLayout(this, BoxLayout.X_AXIS)
        minimumSize = Dimension(320, 40)
        maximumSize = Dimension(1200, 40)

        val open = LIcon("openFile.png")
        open.addActionListener { MainFrame.openFile() }
        add(open)

        val save = LIcon("save.png")
        save.addActionListener { MainFrame.save() }
        add(save)

        val close = LIcon("close.png")
        close.addActionListener { MainFrame.closeFile() }
        add(close)

        val load = LIcon("loadGrammar.png")
        load.addActionListener { MainFrame.loadGrammar() }
        add(load)

        rule.maximumSize = Dimension(160, 32)
        add(rule)

        val process = LIcon("process.png")
        process.addActionListener { MainFrame.process() }
        add(process)

        val optimize = LIcon("optimize.png")
        optimize.addActionListener { MainFrame.optimize() }
        add(optimize)

        tree.addActionListener { Settings.setForce("forcePrint", tree.isSelected) }
        add(tree)
    }

    fun getRule() = rule.text!!
}
