package com.example.kleine.fragments.shopping

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.kleine.R
import com.example.kleine.adapters.viewpager.HomeViewpagerAdapter
import com.example.kleine.databinding.FragmentHomeBinding
import com.example.kleine.fragments.categories.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayoutMediator


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoriesFragments = arrayListOf<Fragment>(
            ChairFragment(),
            CupboardFragment(),
            TableFragment(),
            AccessoryFragment(),
            FurnitureFragment(),
            EnlighteningFragment()
        )
        binding.viewpagerHome.isUserInputEnabled = false
        val fragmentAdapter =
            HomeViewpagerAdapter(categoriesFragments, childFragmentManager, lifecycle)
        binding.viewpagerHome.adapter = fragmentAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewpagerHome) { tab, position ->
            when (position) {
                0 -> tab.text = resources.getString(R.string.g_chair)
                1 -> tab.text = resources.getString(R.string.g_cupboard)
                2 -> tab.text = resources.getString(R.string.g_table)
                3 -> tab.text = resources.getString(R.string.g_accessory)
                4 -> tab.text = resources.getString(R.string.g_furniture)
                5 -> tab.text = resources.getString(R.string.g_enlightening)
            }
        }.attach()


    }

    override fun onResume() {
        super.onResume()

        val bottomNavigation = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.visibility = View.VISIBLE
    }


}