package com.elevenetc.zipher.shared

import android.content.Context
import com.elevenetc.zipher.AppDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory

actual class DatabaseDriverFactory(val context: Context) {
    actual fun createDriver(key: String): SqlDriver {
        return AndroidSqliteDriver(
            AppDatabase.Schema,
            context,
            "app.db",
            factory = SupportFactory(
                SQLiteDatabase.getBytes(key.toCharArray())
            )
        )
    }
}