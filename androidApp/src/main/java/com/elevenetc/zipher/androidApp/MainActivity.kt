package com.elevenetc.zipher.androidApp

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.elevenetc.zipher.androidApp.App.Companion.dao
import com.elevenetc.zipher.shared.Dao
import com.elevenetc.zipher.shared.Greeting
import com.elevenetc.zipher.shared.InvalidDbPassword
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.time.ExperimentalTime
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

fun greet(): String {
    return Greeting().greeting()
}

@ExperimentalTime
class MainActivity : AppCompatActivity(), CoroutineScope {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textStatus: TextView = findViewById(R.id.text_status)
        val editPassword: EditText = findViewById(R.id.edit_password)
        val btnUnlock: Button = findViewById(R.id.btn_unlock)





        dao.runSomBack {
            val currentThread = Thread.currentThread()
            println(it)
        }

        launch {
            collect(dao)
        }


        if (dao.isLocked()) {
            textStatus.text = "DB is locked"
        } else {
            textStatus.text = "DB is unlocked"
        }

        btnUnlock.setOnClickListener {
            val password = editPassword.text.toString()
            try {
                dao.unlock(password)
                textStatus.text = "DB is unlocked"
            } catch (t: InvalidDbPassword) {
                t.printStackTrace()
                textStatus.text = "Password is invalid. Try another one."
            }

        }
    }

    suspend fun collect(dao: Dao) {

        val flowCall = App.dao.flowCall()

        flowCall.collect {
            Log.d(">>>>", it)
        }

        val mutableFlowCall = App.dao.mutableFlowCall()
        mutableFlowCall.collect {
            println(it)
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main
}
