package com.example.kleine.fragments.categories

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kleine.adapters.recyclerview.AdsRecyclerAdapter
import com.example.kleine.adapters.recyclerview.BestDealsRecyclerAdapter
import com.example.kleine.databinding.FragmentChairBinding
import com.example.kleine.firebaseDatabase.FirebaseDb
import com.example.kleine.viewmodel.shopping.ShoppingViewModel
import com.example.kleine.viewmodel.shopping.ShoppingViewModelProviderFactory

class ChairFragment : Fragment() {
    private lateinit var binding: FragmentChairBinding
    private lateinit var adsAdapter: AdsRecyclerAdapter
    private lateinit var viewModel: ShoppingViewModel
    private lateinit var bestDealsAdapter:BestDealsRecyclerAdapter
    private lateinit var chairsAdapter: BestDealsRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database = FirebaseDb()
        val viewModelFactory = ShoppingViewModelProviderFactory(database)
        viewModel = ViewModelProvider(this, viewModelFactory)[ShoppingViewModel::class.java]
        adsAdapter = AdsRecyclerAdapter()
        bestDealsAdapter = BestDealsRecyclerAdapter()
        chairsAdapter = BestDealsRecyclerAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        setUpTimer()
    }

    private fun observeChairs() {
        viewModel.chairs.observe(viewLifecycleOwner, Observer { chairsList->
            chairsAdapter.differ.submitList(chairsList.toList())

        })
    }

    private fun setupChairsRecyclerView() {
        binding.rvChairs.apply {
            layoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
            adapter = chairsAdapter
        }
    }


    private fun setupBestDealsRecyclerView() {
        binding.rvBestDeals.apply {
            layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            adapter = bestDealsAdapter
        }
    }

    private fun setUpTimer() {
        object : CountDownTimer(43140000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                val millis = millisUntilFinished
                updateTimer(millis)
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
        viewModel.bestDeals.observe(viewLifecycleOwner, Observer { bestDealsList->
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