package com.example.kleine.fragments.shopping

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kleine.SpacingDecorator.VerticalSpacingItemDecorator
import com.example.kleine.adapters.recyclerview.CartRecyclerAdapter
import com.example.kleine.databinding.FragmentCartBinding
import com.example.kleine.resource.Resource
import com.example.kleine.viewmodel.shopping.cart.CartViewModel

class CartFragment : Fragment() {
    private lateinit var binding:FragmentCartBinding
    private lateinit var viewModel:CartViewModel
    private lateinit var cartAdapter:CartRecyclerAdapter
    private val TAG = "CartFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this)[CartViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeCart()
    }

    private fun setupRecyclerView() {
        cartAdapter = CartRecyclerAdapter()
        binding.apply {
            rvCart.layoutManager = LinearLayoutManager(context)
            rvCart.adapter = cartAdapter
            rvCart.addItemDecoration(VerticalSpacingItemDecorator(50))
        }
    }

    private fun observeCart() {
        viewModel.cartProducts.observe(viewLifecycleOwner, Observer { response->
            when(response){
                is Resource.Loading -> {
                    showLoading()
                    return@Observer
                }

                is Resource.Success -> {
                    hideLoading()
                    val products = response.data
                    if(products!!.isNotEmpty()) {
                        cartAdapter.differ.submitList(products)
                        var totalPrice = 0
                        products!!.forEach {
                            totalPrice += it.price.toInt() * it.quantity
                            binding.tvTotalprice.text = "$ $totalPrice"
                        }
                    }else{
                        cartAdapter.differ.submitList(products)
                        binding.btnCheckout.visibility = View.INVISIBLE
                        binding.linear.visibility = View.INVISIBLE
                    }
                    return@Observer
                }

                is Resource.Error -> {
                    hideLoading()
                    Log.e(TAG,response.message.toString())
                    Toast.makeText(activity, "Oops error occurred", Toast.LENGTH_SHORT).show()
                    return@Observer
                }
            }
        })
    }

    private fun hideLoading() {
        binding.apply {
            progressBar.visibility = View.GONE
            linear.visibility = View.VISIBLE
            btnCheckout.visibility = View.VISIBLE
        }
    }

    private fun showLoading() {
        binding.apply {
            progressBar.visibility = View.VISIBLE
            linear.visibility = View.INVISIBLE
            btnCheckout.visibility = View.INVISIBLE
        }
    }

}