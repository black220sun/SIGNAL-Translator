package com.blacksun.gui.util

import com.blacksun.settings.Settings
import javax.swing.JCheckBox

open class LCheckBox(name: String, state: Boolean = false) : JCheckBox(Settings.getLang(name), state)