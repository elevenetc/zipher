package com.elevenetc.zipher.shared

import com.elevenetc.zipher.AppDatabase
import com.elevenetc.zipher.Record

class Dao(key: String, dbDriverFactory: DatabaseDriverFactory) {

    private val db = AppDatabase(dbDriverFactory.createDriver(key))
    private val queries = db.appDatabaseQueries

    internal fun clearDb() {
        //
        queries.removeAllRecords()
    }

    fun getAllRecords(): List<Record> {
        return queries.selectAllRecords().executeAsList()
    }

    fun insertRecord(name: String) {
        queries.insertRecord(randomUUID(), name)
    }
}