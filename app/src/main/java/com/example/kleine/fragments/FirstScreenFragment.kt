package com.example.kleine.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.kleine.R
import com.example.kleine.databinding.FragmentFirstScreenBinding
import com.example.kleine.util.Constants.Companion.SHOULD_SHOW
import com.example.kleine.util.Constants.Companion.SPLASH_SHARED_PREF

class FirstScreenFragment : Fragment() {
    private lateinit var binding: FragmentFirstScreenBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFirstScreenBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val shouldShowSplash =
            activity!!.getSharedPreferences(SPLASH_SHARED_PREF, Context.MODE_PRIVATE)
                .getBoolean(SHOULD_SHOW, false)

        if (!shouldShowSplash)
            findNavController().navigate(R.id.action_firstScreenFragment_to_secondScreenFragment)

        else
            binding.btnFirstscreen.setOnClickListener {

                findNavController().navigate(R.id.action_firstScreenFragment_to_secondScreenFragment)

                val sharedPref =
                    activity!!.getSharedPreferences(SPLASH_SHARED_PREF, Context.MODE_PRIVATE)
                sharedPref.edit().putBoolean(SHOULD_SHOW, false).apply()
            }
    }
}