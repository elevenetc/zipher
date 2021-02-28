package com.elevenetc.zipher.shared

import com.elevenetc.zipher.AppDatabase
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import com.squareup.sqldelight.drivers.native.wrapConnection
import co.touchlab.sqliter.DatabaseConfiguration

actual class DatabaseDriverFactory {

    actual fun createDriver(key: String): SqlDriver {
        val schema = AppDatabase.Schema

        val configuration = DatabaseConfiguration(
            name = "app.db",
            version = schema.version,
            create = { connection ->
                wrapConnection(connection) { schema.create(it) }
            },
            upgrade = { connection, oldVersion, newVersion ->
                wrapConnection(connection) { schema.migrate(it, oldVersion, newVersion) }
            },
            key = key,
            rekey = null
        )

        return NativeSqliteDriver(configuration)
    }
}