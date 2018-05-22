package com.blacksun.gui

import com.blacksun.gui.util.LFrame
import javax.swing.JScrollPane
import javax.swing.JTextArea

class ErrorFrame(errors: String): LFrame("Error") {
    init {
        val text = JTextArea(errors)
        text.isEditable = false
        contentPane = JScrollPane(text)
        defaultCloseOperation = DISPOSE_ON_CLOSE
        isVisible = true
        pack()
    }
}
