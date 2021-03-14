package com.elevenetc.zipher.shared

import android.content.Context

actual class DbFileHandler(val fileName: String, val context: Context) {

    actual fun delete() {
        context.deleteDatabase(fileName)
    }

    actual fun getName(): String {
        return fileName
    }

    actual fun exists(): Boolean {
        return context.databaseList().contains(getName())
    }
}