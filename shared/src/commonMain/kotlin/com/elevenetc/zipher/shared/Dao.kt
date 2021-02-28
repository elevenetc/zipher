package com.elevenetc.zipher.shared

import com.elevenetc.zipher.AppDatabase
import com.elevenetc.zipher.Record

class Dao(private val dbDriverFactory: DatabaseDriverFactory) {

    private var db: AppDatabase? = null

    fun clearDb() {
        safeDb().appDatabaseQueries.removeAllRecords()
    }

    @Throws(InvalidDbPassword::class)
    fun unlock(key: String) {
        if (db != null) throw IllegalStateException("DB is unlocked already.")
        try {
            db = AppDatabase(dbDriverFactory.createDriver(key)).apply {
                appDatabaseQueries.checkDb().execute()
            }
        } catch (e: Exception) {
            db = null
            throw InvalidDbPassword(e)
        }
    }

    fun isLocked(): Boolean {
        return db == null
    }

    fun getAllRecords(): List<Record> {
        return safeDb().appDatabaseQueries.selectAllRecords().executeAsList()
    }

    fun insertRecord(name: String) {
        safeDb().appDatabaseQueries.insertRecord(randomUUID(), name)
    }

    private fun safeDb(): AppDatabase {
        if (db == null) throw IllegalStateException("DB is locked")
        else return db!!
    }
}