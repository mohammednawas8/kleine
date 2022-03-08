package com.example.kleine.fragments.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kleine.R
import com.example.kleine.activities.ShoppingActivity
import com.example.kleine.adapters.recyclerview.AdsRecyclerAdapter
import com.example.kleine.adapters.recyclerview.BestDealsRecyclerAdapter
import com.example.kleine.adapters.recyclerview.ProductsRecyclerAdapter
import com.example.kleine.databinding.FragmentHomeProductsBinding
import com.example.kleine.firebaseDatabase.FirebaseDb
import com.example.kleine.util.Constants.Companion.PRODUCT_FLAG
import com.example.kleine.viewmodel.shopping.ShoppingViewModel

class HomeProductsFragment : Fragment() {
    private lateinit var binding: FragmentHomeProductsBinding
    private lateinit var adsAdapter: AdsRecyclerAdapter
    private lateinit var viewModel: ShoppingViewModel
    private lateinit var bestDealsAdapter: BestDealsRecyclerAdapter
    private lateinit var productsAdapter: ProductsRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database = FirebaseDb()
        viewModel = (activity as ShoppingActivity).viewModel
        adsAdapter = AdsRecyclerAdapter()
        bestDealsAdapter = BestDealsRecyclerAdapter()
        productsAdapter = ProductsRecyclerAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupHeaderRecyclerView()
        observeHeaderProducts()

        setupBestDealsRecyclerView()
        observeBestDeals()

        setupAllProductsRecyclerView()
        observeAllProducts()

        adsPaging()
        bestDealsPaging()
        productsPaging()

        observeEmptyAds()
        observeEmptyBestDeals()

        productsAdapter.onItemClick = { product ->
            val bundle = Bundle()
            bundle.putParcelable("product", product)
            bundle.putString("flag", PRODUCT_FLAG)
            findNavController().navigate(
                R.id.action_homeFragment_to_productPreviewFragment2,
                bundle
            )
        }


    }

    private fun observeEmptyBestDeals() {
        viewModel.emptyBestDeals.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                binding.apply {
                    rvBestDeals.visibility = View.GONE
                    tvBestDeals.visibility = View.GONE
                }
            }
        })
    }

    private fun observeEmptyAds() {
        viewModel.emptyClothes.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                binding.apply {
                    rvAds.visibility = View.GONE
                }
            }
        })
    }


    private fun bestDealsPaging() {
        binding.rvBestDeals.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollHorizontally(1) && dx != 0) {
                    viewModel.getBestDealsProduct()
                }
            }
        })
    }

    private fun adsPaging() {
        binding.rvAds.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1) && dx != 0) {
                    viewModel.getClothesProducts()
                }
            }
        })
    }

    private fun productsPaging() {
        binding.scrollChair.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->

            if (v!!.getChildAt(0).bottom <= (v.height + scrollY)) {
                viewModel.getHomeProduct()
            }
        })
    }

    private fun observeAllProducts() {
        viewModel.home.observe(viewLifecycleOwner, Observer { chairsList ->
            productsAdapter.differ.submitList(chairsList.toList())

        })
    }

    private fun setupAllProductsRecyclerView() {
        binding.rvChairs.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = productsAdapter
        }
    }


    private fun setupBestDealsRecyclerView() {
        binding.rvBestDeals.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = bestDealsAdapter
        }
    }




    private fun observeHeaderProducts() {
        viewModel.clothes.observe(viewLifecycleOwner, Observer { clothesList ->
            adsAdapter.differ.submitList(clothesList.toList())
        })
    }

    private fun observeBestDeals() {
        viewModel.bestDeals.observe(viewLifecycleOwner, Observer { bestDealsList ->
            bestDealsAdapter.differ.submitList(bestDealsList.toList())
        })
    }

    private fun setupHeaderRecyclerView() {
        binding.rvAds.apply {
            adapter = adsAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }
}