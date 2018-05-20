package com.blacksun.settings

class Force {
    fun getMode(key: String): Boolean {
        if (!checkKey(key))
            return false
        val value = Settings.getProperty(key)
        if (value == null || !checkValue(value)) {
            Settings.setProperty(key, "false")
            return false
        }
        return value.toBoolean()
    }

    fun setMode(key: String, value: Boolean) {
        if (checkKey(key))
            Settings.setProperty(key, value.toString())
    }

    private fun checkKey(key: String): Boolean = key.startsWith("force")

    private fun checkValue(value: String): Boolean = value.matches("true|false".toRegex())
}