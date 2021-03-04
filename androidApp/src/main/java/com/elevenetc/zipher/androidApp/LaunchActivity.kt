package com.elevenetc.zipher.androidApp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.elevenetc.zipher.androidApp.navigation.Navigator
import com.elevenetc.zipher.shared.Greeting
import com.elevenetc.zipher.shared.LockRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import kotlin.coroutines.CoroutineContext

fun greet(): String {
    return Greeting().greeting()
}

class LauncherActivity : AppCompatActivity(), CoroutineScope {

    val navigator: Navigator by inject()
    val lockRepository: LockRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        if (savedInstanceState == null) {

            launch {
                if (lockRepository.state == LockRepository.State.Locked) {
                    navigator.addRootScreen(LockFragment(), false)
                } else {
                    navigator.addRootScreen(HomeFragment(), false)
                }
            }

        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main
}
