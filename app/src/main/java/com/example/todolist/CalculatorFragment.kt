package com.example.todolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment

class CalculatorFragment : Fragment() {

    private lateinit var display: EditText
    private var operand1 = ""
    private var operand2 = ""
    private var operator = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_calculator, container, false)


        display = view.findViewById(R.id.display)


        val numberClickListener = View.OnClickListener {
            val button = it as Button
            val currentText = display.text.toString()
            display.setText(currentText + button.text)
        }

        view.findViewById<Button>(R.id.button0).setOnClickListener(numberClickListener)
        view.findViewById<Button>(R.id.button1).setOnClickListener(numberClickListener)
        view.findViewById<Button>(R.id.button2).setOnClickListener(numberClickListener)
        view.findViewById<Button>(R.id.button3).setOnClickListener(numberClickListener)
        view.findViewById<Button>(R.id.button4).setOnClickListener(numberClickListener)
        view.findViewById<Button>(R.id.button5).setOnClickListener(numberClickListener)
        view.findViewById<Button>(R.id.button6).setOnClickListener(numberClickListener)
        view.findViewById<Button>(R.id.button7).setOnClickListener(numberClickListener)
        view.findViewById<Button>(R.id.button8).setOnClickListener(numberClickListener)
        view.findViewById<Button>(R.id.button9).setOnClickListener(numberClickListener)


        view.findViewById<Button>(R.id.buttonDot).setOnClickListener {
            val currentText = display.text.toString()
            if (!currentText.contains(".")) {
                display.setText(currentText + ".")
            }
        }


        view.findViewById<Button>(R.id.buttonAdd).setOnClickListener {
            operator = "+"
            operand1 = display.text.toString()
            display.setText("")
        }

        view.findViewById<Button>(R.id.buttonSubtract).setOnClickListener {
            operator = "-"
            operand1 = display.text.toString()
            display.setText("")
        }

        view.findViewById<Button>(R.id.buttonMultiply).setOnClickListener {
            operator = "*"
            operand1 = display.text.toString()
            display.setText("")
        }

        view.findViewById<Button>(R.id.buttonDivide).setOnClickListener {
            operator = "/"
            operand1 = display.text.toString()
            display.setText("")
        }

        view.findViewById<Button>(R.id.buttonEqual).setOnClickListener {
            operand2 = display.text.toString()
            val result = calculateResult(operand1, operator, operand2)
            display.setText(result)
            operand1 = ""
            operand2 = ""
            operator = ""
        }

        view.findViewById<Button>(R.id.buttonClear).setOnClickListener {
            display.setText("")
            operand1 = ""
            operand2 = ""
            operator = ""
        }

        return view
    }

    private fun calculateResult(operand1: String, operator: String, operand2: String): String {
        return when (operator) {
            "+" -> (operand1.toDouble() + operand2.toDouble()).toString()
            "-" -> (operand1.toDouble() - operand2.toDouble()).toString()
            "*" -> (operand1.toDouble() * operand2.toDouble()).toString()
            "/" -> {
                if (operand2.toDouble() == 0.0) {
                    "Error"
                } else {
                    (operand1.toDouble() / operand2.toDouble()).toString()
                }
            }
            else -> "Error"
        }
    }
}
