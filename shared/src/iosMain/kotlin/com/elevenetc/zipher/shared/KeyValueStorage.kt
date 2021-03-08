package com.elevenetc.zipher.shared

import platform.Foundation.NSUserDefaults

actual class KeyValueStorage {

    private val defaults = NSUserDefaults.standardUserDefaults()

    actual fun store(key: String, value: String) {
        defaults.setObject(value, key)
    }

    actual fun get(key: String, defaultValue: String): String {
        return defaults.stringForKey(key) ?: defaultValue
    }

    actual fun store(key: String, value: Boolean) {
        defaults.setBool(value, key)
    }

    actual fun get(key: String, defaultValue: Boolean): Boolean {
        return defaults.boolForKey(key)
    }
}