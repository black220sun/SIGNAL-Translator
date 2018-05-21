package com.blacksun.gui

import javax.swing.JFrame
import javax.swing.JScrollPane
import javax.swing.JTextArea

class ErrorFrame(errors: String): JFrame("Error") {
    init {
        val text = JTextArea(errors)
        text.isEditable = false
        contentPane = JScrollPane(text)
        defaultCloseOperation = DISPOSE_ON_CLOSE
        isVisible = true
        pack()
    }
}
