package com.blacksun.gui

import javax.swing.JScrollPane
import javax.swing.JTextArea

class OutputPanel: JScrollPane() {
    val textArea = JTextArea()
    init {
        viewport.view = textArea
        textArea.isEditable = false
    }
}
