package com.blacksun.gui

import javax.swing.JScrollPane
import javax.swing.JTextArea

class InputPanel: JScrollPane() {
    val textArea = JTextArea()
    init {
        viewport.view = textArea
    }
}