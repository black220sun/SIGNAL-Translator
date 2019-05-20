package com.blacksun.settings

class Language {
    private val lang = HashMap<String, String>()
    private val languages = HashMap<String, String>()
    private val defaultLang = "English"
    val defaultCharset = Charsets.UTF_8

    init {
        languages[defaultLang] = defaultLang
    }

    fun initLang(langPath: String) {
        Settings.loadFile(langPath, languages)
        val lang = Settings.getProperty("langName")
        if (lang == null) {
            Settings.setProperty("langName", defaultLang)
            Settings.setProperty("langActive", defaultLang)
            return
        }
        Settings.setProperty("langActive", lang)
        val path = languages[lang] ?: return
        Settings.loadFile(path, this.lang)
    }

    fun getLang(key: String): String = lang.getOrPut(key) {key}

    fun saveLang() {
        val langName = Settings.getProperty("langActive")
        val filePath = languages[langName] ?: return
        Settings.saveFile(filePath, lang)
    }

    fun getLanguages(): Array<String> = languages.keys.toTypedArray()
    fun getLangName(): String = Settings.getProperty("langName") ?: defaultLang
    fun setLangName(value: String) {
        if (languages.containsKey(value))
            Settings.setProperty("langName", value)
    }
}