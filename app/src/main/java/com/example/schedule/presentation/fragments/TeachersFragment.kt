package com.example.schedule.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.schedule.databinding.FragmentTeachersBinding


class TeachersFragment : Fragment() {

    private lateinit var binding: FragmentTeachersBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTeachersBinding.inflate(inflater, container, false)
        return binding.root
    }
}