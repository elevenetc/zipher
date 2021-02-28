package com.elevenetc.zipher.androidApp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.elevenetc.zipher.shared.Dao
import com.elevenetc.zipher.shared.DatabaseDriverFactory
import com.elevenetc.zipher.shared.Greeting

fun greet(): String {
    return Greeting().greeting()
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tv: TextView = findViewById(R.id.text_view)

        val dao = Dao("123", DatabaseDriverFactory(applicationContext))
        val allRecords = dao.getAllRecords()

        if (allRecords.isEmpty()) {
            dao.insertRecord("first-record")
            tv.text = "First record created: "
        } else {
            val first = allRecords.first()
            tv.text = "Record retrieved: " + first.id
        }

        //tv.text = greet()
    }
}
