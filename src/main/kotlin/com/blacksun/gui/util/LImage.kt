package com.blacksun.gui.util

import com.blacksun.settings.Settings
import javax.swing.ImageIcon

class LImage(path: String) : ImageIcon("${Settings.resources}$path")
