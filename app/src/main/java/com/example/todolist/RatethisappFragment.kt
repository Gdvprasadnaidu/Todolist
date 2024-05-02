package com.example.todolist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RatingBar
import android.widget.Toast

class RatethisappFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ratethisapp, container, false)

        val ratingBar = view.findViewById<RatingBar>(R.id.ratingBar)
        val rateButton = view.findViewById<Button>(R.id.rateButton)

        rateButton.setOnClickListener {
            val rating = ratingBar.rating.toInt()
            when (rating) {
                1 -> {
                    Toast.makeText(requireContext(), "Sorry for your bad experience", Toast.LENGTH_SHORT).show()
                    ratingBar.rating = 1f
                }
                2 -> {
                    Toast.makeText(requireContext(), "We are trying our best to improve our app", Toast.LENGTH_SHORT).show()
                    ratingBar.rating = 2f
                }
                3 -> {
                    Toast.makeText(requireContext(), "We improve ourselves for getting better feedback next time", Toast.LENGTH_SHORT).show()
                    ratingBar.rating = 3f
                }
                4 -> {
                    Toast.makeText(requireContext(), "We are very Happy for this rating", Toast.LENGTH_SHORT).show()
                    ratingBar.rating = 4f
                }
                5 -> {
                    Toast.makeText(requireContext(), "We are very Happy to see our user fully satisfied", Toast.LENGTH_SHORT).show()
                    ratingBar.rating = 5f
                }
            }
        }

        return view
    }
}