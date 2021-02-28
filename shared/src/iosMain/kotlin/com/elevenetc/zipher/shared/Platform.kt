package com.elevenetc.zipher.shared


actual class Platform actual constructor() {
    actual val platform: String
        get() {
            val dao = Dao("123", DatabaseDriverFactory())
            val allRecords = dao.getAllRecords()

            return if (allRecords.isEmpty()) {
                val newId = "hello-ios"
                dao.insertRecord(newId)
                "entry was created: $newId"
            } else {
                val record = allRecords.first()
                val id = record.id
                val name = record.name
                "entry exists \n $name \n $id"
            }
        }
}