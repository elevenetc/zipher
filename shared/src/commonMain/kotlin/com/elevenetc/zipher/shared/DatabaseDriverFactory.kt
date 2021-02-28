package com.elevenetc.zipher.shared

import com.squareup.sqldelight.db.SqlDriver

expect class DatabaseDriverFactory {
    fun createDriver(key:String): SqlDriver
}