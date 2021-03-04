package com.elevenetc.zipher.shared

import android.content.Context

actual class KeyValueStorage(val context: Context) {

    val prefs = context.getSharedPreferences("zipher.prefs", Context.MODE_PRIVATE)

    actual fun store(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    actual fun get(key: String, defaultValue: String): String {
        return prefs.getString(key, defaultValue)!!
    }

    actual fun store(key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }

    actual fun get(key: String, defaultValue: Boolean): Boolean {
        return prefs.getBoolean(key, defaultValue)
    }

}