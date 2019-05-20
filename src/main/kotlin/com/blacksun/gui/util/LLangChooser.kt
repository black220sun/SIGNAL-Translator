package com.blacksun.gui.util

import com.blacksun.settings.Settings
import javax.swing.JComboBox

class LLangChooser: JComboBox<String>() {
    init {
        Settings.getLanguages().forEach(this::addItem)
        selectedItem = Settings.getLangName()
        addActionListener { Settings.setLangName(selectedItem as String) }
    }
}
