package com.blacksun.gui

import java.awt.Dimension
import javax.swing.JScrollPane
import javax.swing.JTextArea

class InputPanel: JScrollPane() {
    val textArea = JTextArea()
    init {
        viewport.view = textArea
        preferredSize = Dimension(550, 700)
    }
}