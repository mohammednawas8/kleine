package com.example.kleine.fragments.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kleine.R
import com.example.kleine.SpacingDecorator.SpacingItemDecorator
import com.example.kleine.activities.ShoppingActivity
import com.example.kleine.adapters.recyclerview.ProductsRecyclerAdapter
import com.example.kleine.databinding.FragmentCupboardBinding
import com.example.kleine.viewmodel.shopping.ShoppingViewModel

class CupboardFragment : Fragment(R.layout.fragment_cupboard) {
    private lateinit var viewModel: ShoppingViewModel
    private lateinit var binding: FragmentCupboardBinding
    private lateinit var mostOrderedCupboardsAdapter: ProductsRecyclerAdapter
    private lateinit var cupboardAdapter:ProductsRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mostOrderedCupboardsAdapter = ProductsRecyclerAdapter()
        cupboardAdapter = ProductsRecyclerAdapter()
        viewModel = (activity as ShoppingActivity).viewModel
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

    }

    private fun cupboardPaging() {
        binding.scrollCupboard.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (v!!.getChildAt(0).bottom <= (v.height + scrollY)) {
                viewModel.getCupboardProduct()
            }
        })
    }

    private fun mostRequestedCupboardPaging() {
        binding.rvCupboardMostOrdered.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if(!recyclerView.canScrollHorizontally(1) && dx!=0)
                    viewModel.getCupboardsByOrders()

            }
        })
    }

    private fun observeCupboard() {
        viewModel.cupboard.observe(viewLifecycleOwner, Observer { cupboardList->
            cupboardAdapter.differ.submitList(cupboardList.toList())
        })
    }

    private fun setupCupboardRecyclerView() {
        binding.rvCupboard.apply {
            adapter = cupboardAdapter
            layoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
        }
    }

    private fun observeMostOrderedCupboard() {
        viewModel.mostCupboardOrdered.observe(viewLifecycleOwner, Observer { cupboardList ->
            mostOrderedCupboardsAdapter.differ.submitList(cupboardList.toList())
        })
    }

    private fun setupMostOrderedCupboardRecyclerView() {
        binding.rvCupboardMostOrdered.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = mostOrderedCupboardsAdapter
            addItemDecoration(SpacingItemDecorator(100))
        }
    }

}