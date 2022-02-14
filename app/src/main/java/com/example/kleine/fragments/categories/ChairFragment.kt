package com.example.kleine.fragments.categories

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kleine.adapters.recyclerview.AdsRecyclerAdapter
import com.example.kleine.adapters.recyclerview.BestDealsRecyclerAdapter
import com.example.kleine.adapters.recyclerview.ProductsRecyclerAdapter
import com.example.kleine.databinding.FragmentChairBinding
import com.example.kleine.firebaseDatabase.FirebaseDb
import com.example.kleine.viewmodel.shopping.ShoppingViewModel
import com.example.kleine.viewmodel.shopping.ShoppingViewModelProviderFactory

class ChairFragment : Fragment() {
    private lateinit var binding: FragmentChairBinding
    private lateinit var adsAdapter: AdsRecyclerAdapter
    private lateinit var viewModel: ShoppingViewModel
    private lateinit var bestDealsAdapter: BestDealsRecyclerAdapter
    private lateinit var productsAdapter: ProductsRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database = FirebaseDb()
        val viewModelFactory = ShoppingViewModelProviderFactory(database)
        viewModel = ViewModelProvider(this, viewModelFactory)[ShoppingViewModel::class.java]
        adsAdapter = AdsRecyclerAdapter()
        bestDealsAdapter = BestDealsRecyclerAdapter()
        productsAdapter = ProductsRecyclerAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChairBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdsRecyclerView()
        observeClothes()

        setupBestDealsRecyclerView()
        observeBestDeals()

        setupChairsRecyclerView()
        observeChairs()

        adsPaging()
        bestDealsPaging()
        chairsPaging()

        observeEmptyAds()
        observeEmptyBestDeals()

        setUpTimer()


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

    private fun chairsPaging() {
        binding.scrollChair.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->

            if (v!!.getChildAt(0).bottom <= (v.height + scrollY)) {
                viewModel.getChairs()
            }
        })
    }

    private fun observeChairs() {
        viewModel.chairs.observe(viewLifecycleOwner, Observer { chairsList ->
            productsAdapter.differ.submitList(chairsList.toList())

        })
    }

    private fun setupChairsRecyclerView() {
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

    private fun setUpTimer() {
        object : CountDownTimer(43140000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                updateTimer(millisUntilFinished)
            }

            override fun onFinish() {
            }
        }.start()
    }

    private fun updateTimer(millis: Long) {
        val hour = millis / 3600000
        val minutes = millis % 3600000 / 60000
        val seconds = millis % 60000 / 1000

        binding.tvOfferTimer.text = "$hour:$minutes:$seconds"
    }

    private fun observeClothes() {
        viewModel.clothes.observe(viewLifecycleOwner, Observer { clothesList ->
            adsAdapter.differ.submitList(clothesList.toList())
        })
    }

    private fun observeBestDeals() {
        viewModel.bestDeals.observe(viewLifecycleOwner, Observer { bestDealsList ->
            bestDealsAdapter.differ.submitList(bestDealsList.toList())
        })
    }

    private fun setupAdsRecyclerView() {
        binding.rvAds.apply {
            adapter = adsAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }
}