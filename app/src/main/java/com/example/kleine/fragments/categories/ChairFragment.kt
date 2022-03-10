package com.example.kleine.fragments.categories

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kleine.R
import com.example.kleine.SpacingDecorator.HorizantalSpacingItemDecorator
import com.example.kleine.activities.ShoppingActivity
import com.example.kleine.adapters.recyclerview.ProductsRecyclerAdapter
import com.example.kleine.databinding.FragmentChairBinding
import com.example.kleine.resource.Resource
import com.example.kleine.util.Constants
import com.example.kleine.viewmodel.shopping.ShoppingViewModel


class ChairFragment : Fragment() {
    val TAG = "ChairFragment"
    private lateinit var viewModel: ShoppingViewModel
    private lateinit var binding: FragmentChairBinding
    private lateinit var headerAdapter: ProductsRecyclerAdapter
    private lateinit var productsAdapter: ProductsRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        headerAdapter = ProductsRecyclerAdapter()
        productsAdapter = ProductsRecyclerAdapter()
        viewModel = (activity as ShoppingActivity).viewModel

        viewModel.getMostRequestedChairs()
        viewModel.getChairs()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChairBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupHeaderRecyclerview()
        observeHeader()

        setupProductsRecyclerView()
        observeProducts()

        headerPaging()
        productsPaging()

        productsAdapter.onItemClick = { product ->
            val bundle = Bundle()
            bundle.putParcelable("product",product)
            bundle.putString("flag", Constants.PRODUCT_FLAG)

            findNavController().navigate(R.id.action_homeFragment_to_productPreviewFragment2,bundle)
        }

        headerAdapter.onItemClick = { product ->
            val bundle = Bundle()
            bundle.putParcelable("product",product)
            bundle.putString("flag", Constants.PRODUCT_FLAG)
            findNavController().navigate(R.id.action_homeFragment_to_productPreviewFragment2,bundle)
        }

    }

    private fun productsPaging() {
        binding.scrollCupboard.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (v!!.getChildAt(0).bottom <= (v.height + scrollY)) {
                viewModel.getChairs(productsAdapter.differ.currentList.size)
            }
        })
    }

    private fun headerPaging() {
        binding.rvHeader.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (!recyclerView.canScrollHorizontally(1) && dx != 0)
                    viewModel.getMostRequestedChairs(headerAdapter.differ.currentList.size)

            }
        })
    }

    private fun observeProducts() {
        viewModel.chairs.observe(viewLifecycleOwner, Observer { response ->

            when (response) {
                is Resource.Loading -> {
                    showBottomLoading()
                    return@Observer
                }

                is Resource.Success -> {
                    hideBottomLoading()
                    productsAdapter.differ.submitList(response.data)
                    return@Observer
                }

                is Resource.Error -> {
                    hideBottomLoading()
                    Log.e(TAG, response.message.toString())
                    return@Observer
                }
            }
        })
    }

    private fun hideBottomLoading() {
        binding.progressbar2.visibility = View.GONE
    }

    private fun showBottomLoading() {
        binding.progressbar2.visibility = View.VISIBLE
    }

    private fun setupProductsRecyclerView() {
        binding.rvProducts.apply {
            adapter = productsAdapter
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        }
    }

    private fun observeHeader() {
        viewModel.mostRequestedChairs.observe(viewLifecycleOwner, Observer { response ->

            when (response) {
                is Resource.Loading -> {
                    showTopLoading()
                    return@Observer
                }

                is Resource.Success -> {
                    hideTopLoading()
                    headerAdapter.differ.submitList(response.data)
                    return@Observer
                }

                is Resource.Error -> {
                    hideTopLoading()
                    Log.e(TAG, response.message.toString())
                    return@Observer
                }
            }
        })
    }

    private fun hideTopLoading() {
        binding.progressbar1.visibility = View.GONE
    }

    private fun showTopLoading() {
        binding.progressbar1.visibility = View.VISIBLE
    }

    private fun setupHeaderRecyclerview() {
        binding.rvHeader.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = headerAdapter
            addItemDecoration(HorizantalSpacingItemDecorator(100))
        }
    }

}