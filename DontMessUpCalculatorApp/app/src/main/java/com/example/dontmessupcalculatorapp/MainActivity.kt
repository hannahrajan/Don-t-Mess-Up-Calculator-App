package com.example.dontmessupcalculatorapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.Debug
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //grab buttons and text view from UI into code via IDs
        var digit = findViewById<Button>(R.id.digit);
        var nextDigit = findViewById<Button>(R.id.nextdigit);
        var operator = findViewById<Button>(R.id.operator);
        var equals = findViewById<Button>(R.id.equals);
        var display = findViewById<TextView>(R.id.display);
        var history = findViewById<Button>(R.id.history);

        val operators = arrayOf(Operators.ADD, Operators.SUBTRACT, Operators.TIMES, Operators.DIVIDE)
        var operatorIndex = operators.size - 1

        var displayCalculation = ""
        var num = 0.0
        var currentCalculation = 0.0
        var currentDigit = 1
        var firstCalculation = true
        var cyclingThroughOperator = false
        var historyList = intent.getStringArrayListExtra("CALCULATIONS") ?: arrayListOf<String>()

        digit.setOnClickListener {
            if (cyclingThroughOperator) {
                cyclingThroughOperator = false
                displayCalculation = display.text.toString()
                num = 0.0
                currentDigit = 1
            } else {
                num = (num + currentDigit) % (currentDigit * 10)
                display.text = displayCalculation + num.toString()
            }
        }

        nextDigit.setOnClickListener {
            currentDigit *= 10
        }

        operator.setOnClickListener {
            if (!cyclingThroughOperator) {
                displayCalculation = display.text.toString()
                cyclingThroughOperator = true
                if (firstCalculation) {
                    currentCalculation = num
                    firstCalculation = false
                } else {
                    currentCalculation = applyOperation(operators[operatorIndex], currentCalculation, num)
                }
            }
            operatorIndex = (operatorIndex + 1) % operators.size
            display.text = displayCalculation + operators[operatorIndex].str
            Log.d(operatorIndex.toString(), "Operator")
        }

        equals.setOnClickListener {
            if (!cyclingThroughOperator) {
                if (firstCalculation) {
                    display.text = num.toString()
                } else {
                    display.text = applyOperation(operators[operatorIndex], currentCalculation, num).toString()
                }
                historyList?.add(displayCalculation + num.toString() + " = " + display.text.toString())
                Log.d("Current List:", historyList.toString())
                displayCalculation = ""
                num = 0.0
                currentCalculation = 0.0
                currentDigit = 1
                operatorIndex = operators.size - 1
                firstCalculation = true
                cyclingThroughOperator = false
            }
        }

        history.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            intent.putStringArrayListExtra("CALCULATIONS", historyList)
            startActivity(intent)
        }


    }

    enum class Operators(val str: String) {
        ADD("+"), SUBTRACT("-"), TIMES("x"), DIVIDE("/")
    }

    fun applyOperation(op: Operators, currentCalculation: Double, num: Double): Double {
        //Log.d(op.str, "current: " + currentCalculation.toString() + ", " + num.toString());
        when (op) {
            Operators.ADD -> return currentCalculation + num
            Operators.SUBTRACT -> return currentCalculation - num
            Operators.TIMES -> return currentCalculation * num
            Operators.DIVIDE -> return currentCalculation / num
        }
    }
}