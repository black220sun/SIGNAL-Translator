package com.blacksun

import java.util.logging.FileHandler
import java.util.logging.Logger
import java.util.logging.SimpleFormatter

object Logger {
    private val logger = Logger.getGlobal()

    fun removeHandlers() {
        logger.useParentHandlers = false
        logger.handlers.forEach { logger.removeHandler(it) }
    }

    fun setOutput(path: String) {
        removeHandlers()
        addOutput(path)
    }

    fun addOutput(path: String) {
        val fileHandler = FileHandler(path)
        fileHandler.formatter = SimpleFormatter()
        logger.addHandler(fileHandler)
    }

    fun info(msg: String) = logger.info(msg)

    fun warn(msg: String) = logger.warning(msg)

    fun err(msg: String) {
        System.err.println(msg)
        logger.severe(msg)
    }
}