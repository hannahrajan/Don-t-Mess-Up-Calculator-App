package com.example.dontmessupcalculatorapp

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class HistoryActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val calc_array = intent.getStringArrayListExtra("CALCULATIONS") ?: arrayListOf()
        val arrayAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            calc_array
        )
        var listView = findViewById<ListView>(R.id.historyList);
        listView.adapter = arrayAdapter

        var back = findViewById<Button>(R.id.back);

        back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putStringArrayListExtra("CALCULATIONS", calc_array)
            startActivity(intent)
        }
    }
}