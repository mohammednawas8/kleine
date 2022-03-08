package com.example.kleine.fragments.shopping

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.kleine.R
import com.example.kleine.activities.ShoppingActivity
import com.example.kleine.adapters.recyclerview.ProductsRecyclerAdapter
import com.example.kleine.databinding.FragmentBlankBinding
import com.example.kleine.resource.Resource
import com.example.kleine.viewmodel.shopping.ShoppingViewModel

class BlankFragment : Fragment() {
    private lateinit var bestProductsAdapter: ProductsRecyclerAdapter
    private lateinit var viewModel: ShoppingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (activity as ShoppingActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return LayoutInflater.from(context).inflate(R.layout.fragment_blank,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    lateinit var binding:FragmentBlankBinding
    fun setData(
        position: Int,
        viewModel: ShoppingViewModel,
        lifeCyclerOwner: LifecycleOwner,
    ) {
        fragmentManager?.beginTransaction()?.replace(R.id.viewPager,this)?.commit()
        bestProductsAdapter = ProductsRecyclerAdapter()
//        binding = FragmentBlankBinding.inflate(LayoutInflater.from(
//            requireContext()
//        ))
//
//        binding.tvBestProducts.setOnClickListener {
//            Toast.makeText(requireContext(), "test", Toast.LENGTH_SHORT).show() }


        Log.d("Test", position.toString())
        viewModel.bestProducts[position - 1].observe(lifeCyclerOwner, Observer { response ->
            Log.d("test",response.data?.size.toString())
            when (response) {
                is Resource.Loading -> {
                    showBottomLoading()
                    return@Observer
                }

                is Resource.Success -> {
                    hideBottomLoading()
                    bestProductsAdapter.differ.submitList(response.data)
                    return@Observer
                }

                is Resource.Error -> {
                    Log.e("Category #$position", response.message.toString())
                    hideBottomLoading()
                    return@Observer
                }
            }
        })

    }

    private fun setupBestProductsRecyclerview(binding: FragmentBlankBinding) {
        binding.rvCupboard.apply {
            adapter = bestProductsAdapter
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        }
    }

    private fun hideTopLoading() {
//        binding.progressbar1.visibility = View.GONE
    }

    private fun showTopLoading() {
//        binding.progressbar1.visibility = View.VISIBLE
    }

    private fun hideBottomLoading() {
//        binding.progressbar2.visibility = View.GONE
    }

    private fun showBottomLoading() {
//        binding.progressbar2.visibility = View.VISIBLE
    }

}