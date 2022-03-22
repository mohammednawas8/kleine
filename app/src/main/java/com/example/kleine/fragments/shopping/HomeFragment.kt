package com.example.kleine.fragments.shopping

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.kleine.R
import com.example.kleine.activities.ShoppingActivity
import com.example.kleine.adapters.viewpager.HomeViewpagerAdapter
import com.example.kleine.databinding.FragmentHomeBinding
import com.example.kleine.fragments.categories.*
import com.example.kleine.fragments.categories.HomeProductsFragment
import com.example.kleine.viewmodel.shopping.ShoppingViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {
    val TAG = "HomeFragment"
    private lateinit var viewModel: ShoppingViewModel
    private lateinit var binding: FragmentHomeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (activity as ShoppingActivity).viewModel
    }
    

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.frameScan.setOnClickListener {
            val snackBar = requireActivity().findViewById<CoordinatorLayout>(R.id.snackBar_coordinator)
            Snackbar.make(snackBar,resources.getText(R.string.g_coming_soon), Snackbar.LENGTH_SHORT).show()
        }
        binding.fragmeMicrohpone.setOnClickListener {
            val snackBar = requireActivity().findViewById<CoordinatorLayout>(R.id.snackBar_coordinator)
            Snackbar.make(snackBar,resources.getText(R.string.g_coming_soon),Snackbar.LENGTH_SHORT).show()
        }

        val categoriesFragments = arrayListOf<Fragment>(
            HomeProductsFragment(),
            ChairFragment(),
            CupboardFragment(),
            TableFragment(),
            AccessoryFragment(),
            FurnitureFragment()
        )
        binding.viewpagerHome.isUserInputEnabled = false

        val viewPager2Adapter =
            HomeViewpagerAdapter(categoriesFragments, childFragmentManager, lifecycle)
        binding.viewpagerHome.adapter = viewPager2Adapter
        TabLayoutMediator(binding.tabLayout,binding.viewpagerHome){tab,position->

            when(position){
                0 -> tab.text = resources.getText(R.string.g_home)
                1-> tab.text = resources.getText(R.string.g_chair)
                2-> tab.text = resources.getText(R.string.g_cupboard)
                3-> tab.text = resources.getText(R.string.g_table)
                4-> tab.text = resources.getText(R.string.g_accessory)
                5-> tab.text = resources.getText(R.string.g_furniture)
                6-> tab.text = resources.getText(R.string.g_enlightening)
            }

        }.attach()


            binding.tvSearch.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
            }

    }


    override fun onResume() {
        super.onResume()
        val bottomNavigation =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()

    }

}