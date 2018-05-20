package com.blacksun.gui.util

import com.blacksun.settings.Settings
import javax.swing.JFileChooser
import java.io.File

class SFileChooser: JFileChooser() {
    fun getFile(): File {
        Settings.setProperty("fileDir", selectedFile.parent)
        return selectedFile
    }

    init {
        currentDirectory = File(Settings.getProperty("fileDir") ?: ".")
    }
}
