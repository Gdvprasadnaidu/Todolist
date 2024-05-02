package com.example.todolist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton


class HomeFragment : Fragment() {

    private lateinit var addFragment: AddFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        addFragment = AddFragment()

        view.findViewById<ImageButton>(R.id.imageButton)?.setOnClickListener {
            navigateToAddFragment()
        }

        return view
    }

    private fun navigateToAddFragment() {

        parentFragmentManager.beginTransaction()
            .replace(R.id.framecontainer, addFragment)
            .addToBackStack(null)
            .commit()
    }
}