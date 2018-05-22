package com.blacksun.gui.util

import com.blacksun.settings.Settings

class ForceCheckBox(name: String): LCheckBox(name) {
    init {
        val force = "force$name"

        isSelected = Settings.getForce(force)

        addActionListener {
            Settings.setForce(force, isSelected)
        }
    }
}