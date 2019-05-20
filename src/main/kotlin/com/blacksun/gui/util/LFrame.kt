package com.blacksun.gui.util

import com.blacksun.settings.Settings
import javax.swing.JFrame

open class LFrame(name: String): JFrame(Settings.getLang(name))