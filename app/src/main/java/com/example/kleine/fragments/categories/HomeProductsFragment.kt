package com.example.kleine.fragments.categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
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
import com.example.kleine.model.CartProduct
import com.example.kleine.resource.Resource
import com.example.kleine.util.Constants.Companion.PRODUCT_FLAG
import com.example.kleine.viewmodel.shopping.ShoppingViewModel
import com.google.android.material.snackbar.Snackbar

class HomeProductsFragment : Fragment() {
    private lateinit var binding: FragmentHomeProductsBinding
    private lateinit var headerAdapter: AdsRecyclerAdapter
    private lateinit var viewModel: ShoppingViewModel
    private lateinit var bestDealsAdapter: BestDealsRecyclerAdapter
    private lateinit var productsAdapter: ProductsRecyclerAdapter
    private val TAG = "HomeProductsFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database = FirebaseDb()
        viewModel = (activity as ShoppingActivity).viewModel
        headerAdapter = AdsRecyclerAdapter()
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

        binding.tvBestDeals.visibility = View.GONE

        setupHeaderRecyclerView()
        observeHeaderProducts()

        setupBestDealsRecyclerView()
        observeBestDeals()

        setupAllProductsRecyclerView()
        observeAllProducts()

        headerPaging()
        bestDealsPaging()
        productsPaging()

        observeEmptyHeader()
        observeEmptyBestDeals()

        onHeaderProductClick()
        onBestDealsProductClick()

        observeAddHeaderProductsToCart()


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

    private fun observeAddHeaderProductsToCart() {
        viewModel.addToCart.observe(viewLifecycleOwner, Observer { response ->

            when (response) {
                is Resource.Loading -> {
                    showTopScreenProgressbar()
                    return@Observer
                }

                is Resource.Success -> {
                    hideTopScreenProgressbar()
                    val snackBarPosition = requireActivity().findViewById<CoordinatorLayout>(R.id.snackBar_coordinator)
                    Snackbar.make(snackBarPosition,requireContext().getText(R.string.product_added),2500).show()
                    return@Observer
                }

                is Resource.Error -> {
                    hideTopScreenProgressbar()
                    return@Observer
                }
            }
        })
    }

    private fun hideTopScreenProgressbar() {

    }

    private fun showTopScreenProgressbar() {

    }

    private fun onBestDealsProductClick() {
        bestDealsAdapter.onItemClick = { product ->
            val bundle = Bundle()
            bundle.putParcelable("product", product)
            findNavController().navigate(
                R.id.action_homeFragment_to_productPreviewFragment2,
                bundle
            )

        }
    }

    private fun onHeaderProductClick() {
        headerAdapter.onItemClick = { product ->
            val bundle = Bundle()
            bundle.putParcelable("product", product)
            findNavController().navigate(
                R.id.action_homeFragment_to_productPreviewFragment2,
                bundle
            )
        }

        headerAdapter.onAddToCartClick = { product ->
            val image = (product.images?.get("images") as List<String>)[0]
            val cartProduct = CartProduct(
                product.id,
                product.title!!,
                product.seller!!,
                image,
                product.price!!,
                product.newPrice,
                1,
                "",
                ""
            )
            viewModel.addProductToCart(cartProduct)
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

    private fun observeEmptyHeader() {
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

    private fun headerPaging() {
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
                viewModel.getHomeProduct(productsAdapter.differ.currentList.size)
            }
        })
    }

    private fun observeAllProducts() {
        viewModel.home.observe(viewLifecycleOwner, Observer { response ->

            when (response) {
                is Resource.Loading -> {
                    showBottomLoading()

                    return@Observer
                }

                is Resource.Success -> {
                    hideBottomLoading()
                    productsAdapter.differ.submitList(response.data)
                    Log.d("test", response.data?.size.toString())
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
        binding.tvBestProducts.visibility = View.VISIBLE

    }

    private fun showBottomLoading() {
        binding.progressbar2.visibility = View.VISIBLE
        binding.tvBestProducts.visibility = View.GONE
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
            headerAdapter.differ.submitList(clothesList.toList())
        })
    }

    private fun observeBestDeals() {
        viewModel.bestDeals.observe(viewLifecycleOwner, Observer { bestDealsList ->
            bestDealsAdapter.differ.submitList(bestDealsList.toList())
            binding.tvBestDeals.visibility = View.VISIBLE
        })
    }

    private fun setupHeaderRecyclerView() {
        binding.rvAds.apply {
            adapter = headerAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }
}