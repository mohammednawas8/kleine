package com.example.kleine.fragments.shopping

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kleine.activities.ShoppingActivity
import com.example.kleine.adapters.viewpager.AdsRecyclerAdapter
import com.example.kleine.databinding.FragmentHomeBinding
import com.example.kleine.firebaseDatabase.FirebaseDb
import com.example.kleine.viewmodel.lunchapp.ViewModelProviderFactory
import com.example.kleine.viewmodel.shopping.ShoppingViewModel
import com.example.kleine.viewmodel.shopping.ShoppingViewModelProviderFactory


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var adsAdapter:AdsRecyclerAdapter
    private lateinit var viewModel:ShoppingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as ShoppingActivity).viewModel
        adsAdapter = AdsRecyclerAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeClothes()
    }

    private fun observeClothes() {
        viewModel.clothes.observe(viewLifecycleOwner, Observer { clothesList->
            adsAdapter.differ.submitList(clothesList.toList())
        })
    }

    private fun setupRecyclerView() {
        binding.rvAds.apply {
            adapter = adsAdapter
            layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        }
    }


}