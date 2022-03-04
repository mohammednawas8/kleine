package com.example.kleine.fragments.settings

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kleine.R
import com.example.kleine.databinding.FragmentLanguageBinding
import com.example.kleine.helpers.LocaleHelper
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*


class LanguageFragment : Fragment() {
    private lateinit var binding:FragmentLanguageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLanguageBinding.inflate(inflater)
        activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation)?.visibility = View.GONE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentLanguage = activity?.getSharedPreferences("Language",Context.MODE_PRIVATE)
            ?.getString("language","English")


        when(currentLanguage){
            "en" -> {
                changeToEnglish()
            }

            "ar" -> {
                changeToArabic()
            }
        }

        binding.linearArabic.setOnClickListener {
            changeLanguage("ar")
        }

        binding.linearEnglish.setOnClickListener {
            changeLanguage("en")
        }

    }

    private fun changeToArabic() {
        binding.apply {
            imgArabic.visibility = View.VISIBLE
            imgEnglish.visibility = View.INVISIBLE
        }
    }

    private fun changeToEnglish() {
        binding.apply {
            imgArabic.visibility = View.INVISIBLE
            imgEnglish.visibility = View.VISIBLE
        }
    }

    private fun changeLanguage(code:String){
        LocaleHelper.setLocale(requireContext(),code)
        if(code == "en")
            changeToEnglish()
        else if(code == "ar")
            changeToArabic()
    }
}