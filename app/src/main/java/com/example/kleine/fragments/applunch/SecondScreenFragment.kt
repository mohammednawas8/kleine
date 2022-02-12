package com.example.kleine.fragments.applunch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.kleine.R
import com.example.kleine.databinding.FragmentSecondScreenBinding

class SecondScreenFragment: Fragment() {
    private lateinit var binding: FragmentSecondScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSecondScreenBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSplashRegister.setOnClickListener {
            findNavController().navigate(R.id.action_secondScreenFragment_to_registerFragment)
        }

        binding.btnSplashSignIn.setOnClickListener {
            findNavController().navigate(R.id.action_secondScreenFragment_to_loginFragment)
        }
    }
}