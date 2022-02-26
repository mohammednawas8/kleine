package com.example.kleine.fragments.shopping

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kleine.R
import com.example.kleine.SpacingDecorator.HorizantalSpacingItemDecorator
import com.example.kleine.adapters.recyclerview.ShippingAddressesAdapter
import com.example.kleine.databinding.FragmentBillingBinding
import com.example.kleine.resource.Resource
import com.example.kleine.viewmodel.billingViewmodel.BillingViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class BillingFragment : Fragment() {
    val TAG = "BillingFragment"
    private lateinit var binding: FragmentBillingBinding
    private lateinit var shippingAddressesAdapter:ShippingAddressesAdapter
    private lateinit var viewModel:BillingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this)[BillingViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBillingBinding.inflate(inflater)
        val bottomNavigation = activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation?.visibility = View.INVISIBLE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onAddAddressImgClick()
        onImgCloseClick()
        setupRecyclerview()

        observeAddresses()
    }

    private fun observeAddresses() {
        viewModel.addresses.observe(viewLifecycleOwner, Observer {
                response ->
            when (response) {
                is Resource.Loading -> {
                    showLoading()
                    return@Observer
                }

                is Resource.Success -> {
                    hideLoading()
                    shippingAddressesAdapter.differ.submitList(response.data!!.toList())
                    return@Observer
                }

                is Resource.Error -> {
                    hideLoading()
                    Log.e(TAG,response.message.toString())
                    Toast.makeText(activity, "Error occurred", Toast.LENGTH_SHORT).show()
                    return@Observer
                }
            }
        })
    }

    private fun hideLoading() {
        binding.progressbarAddresses.visibility = View.GONE
    }

    private fun showLoading() {
        binding.progressbarAddresses.visibility = View.VISIBLE
    }

    private fun setupRecyclerview() {
        shippingAddressesAdapter = ShippingAddressesAdapter()
        binding.rvAdresses.apply {
            adapter = shippingAddressesAdapter
            layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            addItemDecoration(HorizantalSpacingItemDecorator(23))
        }
    }

    private fun onImgCloseClick() {
        binding.imgCloseBilling.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun onAddAddressImgClick() {
        binding.imgAddAddress.setOnClickListener {
            findNavController().navigate(R.id.action_billingFragment_to_addressFragment)
        }
    }


}