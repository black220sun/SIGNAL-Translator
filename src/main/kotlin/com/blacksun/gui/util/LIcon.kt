package com.blacksun.gui.util

import com.blacksun.settings.Settings
import java.awt.Dimension
import javax.swing.ImageIcon
import javax.swing.JButton
import java.io.File

class LIcon(path: String): JButton() {
    init {
        val file = File(Settings.resources + path)
        val name = Settings.getLang(file.nameWithoutExtension
                .map { if (it in 'A'..'Z') " ${it.toLowerCase()}" else "$it"}
                .joinToString("")
                .capitalize())
        if (file.exists()) {
            icon = ImageIcon(file.absolutePath)
            preferredSize = Dimension(icon.iconWidth, icon.iconHeight)
            maximumSize = preferredSize
            minimumSize = preferredSize
        } else
            text = name
        toolTipText = name
    }
}