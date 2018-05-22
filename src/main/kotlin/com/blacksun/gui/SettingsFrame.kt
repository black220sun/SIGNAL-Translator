package com.blacksun.gui

import com.blacksun.gui.util.ForceCheckBox as F
import com.blacksun.gui.util.LFrame
import javax.swing.BoxLayout
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JScrollPane

class SettingsFrame: LFrame("Settings") {
    init {
        val panel = JPanel()
        contentPane = JScrollPane(panel)
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)

        panel.add(F("Quit"))
        panel.add(F("Print"))

        panel.add(JLabel(""))

        panel.add(F("OptimizeEmpty"))
        panel.add(F("OptimizeEmptyElse"))
        panel.add(F("OptimizeEmptyThen"))
        panel.add(F("OptimizeEmptyIf"))
        panel.add(F("OptimizeEmptyLoop"))
        panel.add(F("OptimizeEmptyWhile"))
        panel.add(F("OptimizeEmptyAlternative"))
        panel.add(F("OptimizeEmptyCase"))

        isVisible = true
        pack()
    }
}