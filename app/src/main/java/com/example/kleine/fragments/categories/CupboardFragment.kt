package com.example.kleine.fragments.categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kleine.R
import com.example.kleine.SpacingDecorator.HorizantalSpacingItemDecorator
import com.example.kleine.activities.ShoppingActivity
import com.example.kleine.adapters.recyclerview.ProductsRecyclerAdapter
import com.example.kleine.databinding.FragmentCupboardBinding
import com.example.kleine.resource.Resource
import com.example.kleine.util.Constants
import com.example.kleine.viewmodel.shopping.ShoppingViewModel

class CupboardFragment : Fragment(R.layout.fragment_cupboard) {
    private lateinit var viewModel: ShoppingViewModel
    private lateinit var binding: FragmentCupboardBinding
    private lateinit var mostOrderedCupboardsAdapter: ProductsRecyclerAdapter
    private lateinit var cupboardAdapter: ProductsRecyclerAdapter
    private val TAG = "CupboardFragment"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mostOrderedCupboardsAdapter = ProductsRecyclerAdapter()
        cupboardAdapter = ProductsRecyclerAdapter()
        viewModel = (activity as ShoppingActivity).viewModel

        viewModel.getMostRequestedCupboards()
        viewModel.getCupboardProduct()
        Log.d("test","cupboardFragment")

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentCupboardBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupMostOrderedCupboardRecyclerView()
        observeMostOrderedCupboard()

        setupCupboardRecyclerView()
        observeCupboard()

        mostRequestedCupboardPaging()
        cupboardPaging()

        cupboardAdapter.onItemClick = { product ->
            val bundle = Bundle()
            bundle.putParcelable("product",product)
            bundle.putString("flag", Constants.PRODUCT_FLAG)

            findNavController().navigate(R.id.action_homeFragment_to_productPreviewFragment2,bundle)
        }

        mostOrderedCupboardsAdapter.onItemClick = { product ->
            val bundle = Bundle()
            bundle.putParcelable("product",product)
            bundle.putString("flag", Constants.PRODUCT_FLAG)
            findNavController().navigate(R.id.action_homeFragment_to_productPreviewFragment2,bundle)
        }

    }

    private fun cupboardPaging() {
        binding.scrollCupboard.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (v!!.getChildAt(0).bottom <= (v.height + scrollY)) {
                viewModel.getCupboardProduct(cupboardAdapter.differ.currentList.size)
            }
        })
    }

    private fun mostRequestedCupboardPaging() {
        binding.rvHeader.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (!recyclerView.canScrollHorizontally(1) && dx != 0)
                    viewModel.getMostRequestedCupboards(mostOrderedCupboardsAdapter.differ.currentList.size)

            }
        })
    }

    private fun observeCupboard() {
        viewModel.cupboard.observe(viewLifecycleOwner, Observer { response ->

            when (response) {
                is Resource.Loading -> {
                    showBottomLoading()
                    return@Observer
                }

                is Resource.Success -> {
                    hideBottomLoading()
                    cupboardAdapter.differ.submitList(response.data)
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

    private fun setupCupboardRecyclerView() {
        binding.rvProducts.apply {
            adapter = cupboardAdapter
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        }
    }

    private fun observeMostOrderedCupboard() {
        viewModel.mostRequestedCupboard.observe(viewLifecycleOwner, Observer { response ->

            when (response) {
                is Resource.Loading -> {
                    showTopLoading()
                    return@Observer
                }

                is Resource.Success -> {
                    hideTopLoading()
                    mostOrderedCupboardsAdapter.differ.submitList(response.data)
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

    private fun setupMostOrderedCupboardRecyclerView() {
        binding.rvHeader.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = mostOrderedCupboardsAdapter
            addItemDecoration(HorizantalSpacingItemDecorator(100))
        }
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    override fun onResume() {
        super.onResume()


    }

}