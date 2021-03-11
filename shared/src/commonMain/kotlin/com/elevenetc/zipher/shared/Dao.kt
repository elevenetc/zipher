package com.elevenetc.zipher.shared

import com.elevenetc.zipher.AppDatabase
import com.elevenetc.zipher.Record
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlin.time.ExperimentalTime
import kotlin.time.TimeSource

class Dao(private val dbDriverFactory: DatabaseDriverFactory) {

    private var db: AppDatabase? = null

    @ExperimentalTime
    fun runSomBack(handler: (Long) -> Unit) {
        GlobalScope.launch(Background) {
            //TimeSource.
            val mark = TimeSource.Monotonic.markNow()
            delay(5000)
            GlobalScope.launch(Main) {
                handler(mark.elapsedNow().inMilliseconds.toLong())
            }
        }
    }

    fun flowCall(): Flow<String> = flow {
        (1..10).forEach {
            println(">>>>: emitting: $it")
            emit(it.toString())
            delay(1000)
        }
    }.flowOn(Background)

    fun mutableFlowCall(): MutableStateFlow<String> {
        return MutableStateFlow("zed")
    }

    fun getById(id: String): Record? {
        return safeDb().appDatabaseQueries.selectRecordById(id).executeAsOneOrNull()
    }

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

    fun lock() {
        db = null
    }

    fun isLocked(): Boolean {
        return db == null
    }

    fun update(record: Record) {
        safeDb().appDatabaseQueries.update(record.name, record.id)
    }

    fun deleteById(id: String) {
        safeDb().appDatabaseQueries.removeById(id)
    }

    fun getAllRecords(): Flow<List<Record>> = flow {
        emit(safeDb().appDatabaseQueries.selectAllRecords().executeAsList())
    }.flowOn(Background)

    fun getAllRecordsSync(): List<Record> {
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