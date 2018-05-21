package com.blacksun.gui

import java.awt.Dimension
import javax.swing.JScrollPane
import javax.swing.JTextArea

class OutputPanel(text: String = ""): JScrollPane() {
    private val textArea = JTextArea(text)
    init {
        viewport.view = textArea
        textArea.isEditable = false
        preferredSize = Dimension(550, 700)
    }
}
