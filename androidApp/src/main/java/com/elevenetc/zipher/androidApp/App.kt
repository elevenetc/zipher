package com.elevenetc.zipher.androidApp

import android.app.Application
import com.elevenetc.zipher.shared.Dao
import com.elevenetc.zipher.shared.DatabaseDriverFactory

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        val dbDriverFactory = DatabaseDriverFactory(this)
        dao = Dao(dbDriverFactory)
    }

    companion object {
        lateinit var dao:Dao
    }
}