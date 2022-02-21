package com.example.kleine.fragments.shopping

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kleine.R
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

        onCloseImgClick()

        onPlusClick()
        onMinusClick()
        onItemClick()

    }

    private fun observePlus() {
        viewModel.plus.observe(viewLifecycleOwner, Observer { response->

            when (response) {
                is Resource.Success -> {
                    binding.apply {
                        progressPlusMinus.visibility = View.INVISIBLE
                    }
                    return@Observer
                }

                is Resource.Loading -> {
                    binding.apply {
                        progressPlusMinus.visibility = View.VISIBLE

                    }
                    return@Observer
                }

                is Resource.Error -> {
                    binding.apply {
                        progressPlusMinus.visibility = View.INVISIBLE
                        Log.e(TAG,response.message.toString())
                    }
                    return@Observer
                }
            }
        })
    }

    private fun observeMinus() {
        viewModel.minus.observe(viewLifecycleOwner, Observer { response->

            when (response) {
                is Resource.Success -> {
                    binding.apply {
                        progressPlusMinus.visibility = View.INVISIBLE
                    }
                    return@Observer
                }

                is Resource.Loading -> {
                    binding.apply {
                        progressPlusMinus.visibility = View.VISIBLE

                    }
                    return@Observer
                }

                is Resource.Error -> {
                    binding.apply {
                        progressPlusMinus.visibility = View.INVISIBLE
                        Log.e(TAG,response.message.toString())
                    }
                    return@Observer
                }
            }
        })
    }


    private fun onItemClick() {
        cartAdapter.onItemClick = {
            Toast.makeText(activity, "Item click", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onMinusClick() {
        cartAdapter.onMinusesClick = { cartProduct->
            if(cartProduct.quantity > 1) {
                viewModel.decreaseQuantity(cartProduct)
                observeMinus()
            }else{
                val alertDialog = AlertDialog.Builder(requireContext()).create()
                alertDialog.setTitle("")
                val view = LayoutInflater.from(context).inflate(R.layout.delete_alert_dialog,null,false)
                alertDialog.setView(view)

                view.findViewById<Button>(R.id.btn_no).setOnClickListener {
                    alertDialog.dismiss()
                }

                view.findViewById<Button>(R.id.btn_yes).setOnClickListener {
                    viewModel.deleteProductFromCart(cartProduct)
                    alertDialog.dismiss()
                }

                alertDialog.show()
            }
        }
    }

    private fun onPlusClick() {
        cartAdapter.onPlusClick = { cartProduct->
            viewModel.increaseQuantity(cartProduct)
            observePlus()
        }
    }

    private fun onCloseImgClick() {
        binding.imgCloseCart.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupRecyclerView() {
        cartAdapter = CartRecyclerAdapter(viewLifecycleOwner,viewModel)
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
                        binding.apply {
                            btnCheckout.visibility = View.INVISIBLE
                            linear.visibility = View.INVISIBLE
                            imgEmptyBox.visibility = View.VISIBLE
                            imgEmptyBoxTexture.visibility = View.VISIBLE
                            tvEmptyCart.visibility = View.VISIBLE
                        }

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
            imgEmptyBox.visibility = View.GONE
            imgEmptyBoxTexture.visibility = View.GONE
            tvEmptyCart.visibility = View.GONE
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