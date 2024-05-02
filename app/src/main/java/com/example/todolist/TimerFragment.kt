package com.example.todolist

import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class TimerFragment : Fragment() {

    private lateinit var timerTextView: TextView
    private lateinit var startButton: Button
    private lateinit var stopButton: Button
    private lateinit var resetButton: Button

    private var isTimerRunning = false
    private var startTime: Long = 0
    private var timeInMilliseconds: Long = 0
    private var timeSwapBuff: Long = 0
    private var updateTime = 0L
    private val handler = Handler()

    private val runnable = object : Runnable {
        override fun run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime
            updateTime = timeSwapBuff + timeInMilliseconds
            val milliseconds = (updateTime % 1000) / 10 // Get only two digits
            val seconds = (updateTime / 1000)
            val minutes = seconds / 60
            val hours = minutes / 60
            timerTextView.text = String.format(
                "%02d:%02d:%02d:%02d",
                hours % 24,
                minutes % 60,
                seconds % 60,
                milliseconds
            )
            handler.postDelayed(this, 0)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_timer, container, false)

        timerTextView = view.findViewById(R.id.timerTextView)
        startButton = view.findViewById(R.id.startButton)
        stopButton = view.findViewById(R.id.stopButton)
        resetButton = view.findViewById(R.id.resetButton)

        startButton.setOnClickListener {
            startTimer()
        }

        stopButton.setOnClickListener {
            stopTimer()
        }

        resetButton.setOnClickListener {
            resetTimer()
        }

        return view
    }

    private fun startTimer() {
        if (!isTimerRunning) {
            startTime = SystemClock.uptimeMillis()
            handler.postDelayed(runnable, 0)
            isTimerRunning = true
        }
    }

    private fun stopTimer() {
        if (isTimerRunning) {
            timeSwapBuff += timeInMilliseconds
            handler.removeCallbacks(runnable)
            isTimerRunning = false
        }
    }

    private fun resetTimer() {
        if (!isTimerRunning) {
            timeSwapBuff = 0
            handler.removeCallbacks(runnable)
            timerTextView.text = "00:00:00:00"
        }
    }
}
