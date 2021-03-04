package com.elevenetc.zipher.androidApp

import android.app.Application
import android.content.Context
import com.elevenetc.zipher.androidApp.navigation.Navigator
import com.elevenetc.zipher.shared.*
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        val dbDriverFactory = DatabaseDriverFactory(this)
        dao = Dao(dbDriverFactory)

        val navigator = Navigator(
            Navigator.Config(
                this,
                LauncherActivity::class.java,
                R.id.root
            )
        )

        val app: Context = this

        val appModule = module {
            single { app }
            single { dao }
            single { KeyValueStorage(app) }
            //single { database.recordDao() }
            //single { RecordsRepository(get()) }
            single { navigator }
            single { LockRepository(get(), get()) }
            factory { LockViewModel(get()) }
            //factory { SettingsViewModel(get()) }
            //factory { DetailsViewModel(get()) }
        }

        startKoin {
            modules(appModule)
        }
    }

    companion object {
        lateinit var dao: Dao
    }
}