package com.blacksun.settings

import java.io.*
import javax.swing.JOptionPane

object Settings {
    private val separator = System.getProperty("file.separator")!!
    private const val csv = ","
    private val home = System.getProperty("user.home")!!
    private val directory = "$home$separator.SIGNAL$separator"
    private val properties = LinkedHashMap<String, String>()
    private val settingsPath = directory + "settings"
    private val lang = Language()
    private val force = Force()
    val resources = directory + "res" + separator
    val defaultCharset = lang.defaultCharset
    init {
        val dir = File(directory)
        if (dir.isFile) {
            JOptionPane.showMessageDialog(null, "Can`t init settings. Working directory is a file",
                    "Error", JOptionPane.ERROR_MESSAGE)
            System.exit(1)
        }
        if (!dir.exists())
            dir.mkdirs()
        init()
    }

    private fun init() {
        loadFile(settingsPath, properties)
        val langAvalPath = properties["languages"]
        if (langAvalPath != null)
            lang.initLang(langAvalPath)
    }

    fun loadFile(filePath: String, table: HashMap<String, String>) {
        val path =
                if (filePath.matches("^(/|[A-H]:\\\\).*".toRegex()))
                    filePath
                else
                    directory + filePath
        val file = File(path)
        if (!file.exists() || !file.isFile)
            return
        val reader = InputStreamReader(file.inputStream(), defaultCharset)
        reader.readLines()
                .filter { it.matches("[^$csv]+$csv[^$csv]+".toRegex()) }
                .forEach {
                    val parts = it.split(csv)
                    table[parts[0]] = parts[1]
                }
        reader.close()
    }

    fun saveFile(filePath: String, table: HashMap<String, String>) {
        val path =
                if (filePath.matches("^(/|[A-H]:\\\\).*".toRegex()))
                    filePath
                else
                    directory + filePath
        OutputStreamWriter(FileOutputStream(path), lang.defaultCharset).use {
            table.forEach { key, value -> it.appendln("$key$csv$value") }
        }
    }

    fun save() {
        saveFile(settingsPath, properties)
    }

    fun getProperty(key: String): String? = properties[key]

    fun setProperty(key: String, value: String) = properties.put(key, value)

    fun getDirectory(): String = directory

    fun getLang(key: String): String = lang.getLang(key)
    fun getLanguages(): Array<String> = lang.getLanguages()
    fun getLangName(): String = lang.getLangName()
    fun setLangName(value: String) = lang.setLangName(value)
    fun saveLang() = lang.saveLang()
    fun getForce(key: String): Boolean = force.getMode(key)
    fun setForce(key: String, value: Boolean) = force.setMode(key, value)
    fun getProperties(pattern: String) =
            properties.filter { it.key.matches(Regex(pattern)) }.map { it.key }
    fun showHidden(): Boolean = properties.getOrPut("showHidden", {"false"}).toBoolean()
}