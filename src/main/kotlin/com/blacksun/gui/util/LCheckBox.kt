package com.blacksun.gui.util

import com.blacksun.settings.Settings
import javax.swing.JCheckBox

class LCheckBox(name: String, state: Boolean = false) : JCheckBox(Settings.getLang(name), state)