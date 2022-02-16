package com.example.kleine.fragments.shopping

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kleine.R
import com.example.kleine.databinding.FragmentProductPreviewBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProductPreviewFragment : Fragment() {
    private lateinit var binding:FragmentProductPreviewBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProductPreviewBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNavigation = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.visibility = View.GONE


        

    }

}