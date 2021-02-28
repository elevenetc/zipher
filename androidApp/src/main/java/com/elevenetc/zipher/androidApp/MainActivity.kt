package com.elevenetc.zipher.androidApp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.elevenetc.zipher.androidApp.App.Companion.dao
import com.elevenetc.zipher.shared.Greeting
import com.elevenetc.zipher.shared.InvalidDbPassword

fun greet(): String {
    return Greeting().greeting()
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textStatus: TextView = findViewById(R.id.text_status)
        val editPassword: EditText = findViewById(R.id.edit_password)
        val btnUnlock: Button = findViewById(R.id.btn_unlock)



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
}
