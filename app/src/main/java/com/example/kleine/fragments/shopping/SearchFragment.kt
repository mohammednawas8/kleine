package com.example.kleine.fragments.shopping

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kleine.R
import com.example.kleine.SpacingDecorator.VerticalSpacingItemDecorator
import com.example.kleine.activities.ShoppingActivity
import com.example.kleine.adapters.recyclerview.CategoriesRecyclerAdapter
import com.example.kleine.adapters.recyclerview.SearchRecyclerAdapter
import com.example.kleine.databinding.FragmentSearchBinding
import com.example.kleine.resource.Resource
import com.example.kleine.viewmodel.shopping.ShoppingViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*

class SearchFragment : Fragment() {
    private val TAG = "SearchFragment"
    private lateinit var binding: FragmentSearchBinding
    private lateinit var inputMethodManger: InputMethodManager
    private lateinit var viewModel: ShoppingViewModel
    private lateinit var categoriesAdapter: CategoriesRecyclerAdapter
    private lateinit var searchAdapter: SearchRecyclerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as ShoppingActivity).viewModel
        viewModel.getCategories()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCategoryRecyclerView()
        setupSearchRecyclerView()
        showKeyboardAutomatically()
        onHomeClick()

        searchProducts()
        observeSearch()

        observeCategories()

        onSearchTextClick()

        onCancelTvClick()

        onCategoryClick()

        binding.frameScan.setOnClickListener {
            val snackBar = requireActivity().findViewById<CoordinatorLayout>(R.id.snackBar_coordinator)
            Snackbar.make(snackBar,resources.getText(R.string.g_coming_soon), Snackbar.LENGTH_SHORT).show()
        }
        binding.fragmeMicrohpone.setOnClickListener {
            val snackBar = requireActivity().findViewById<CoordinatorLayout>(R.id.snackBar_coordinator)
            Snackbar.make(snackBar,resources.getText(R.string.g_coming_soon), Snackbar.LENGTH_SHORT).show()
        }

    }

    private fun onCategoryClick() {
        categoriesAdapter.onItemClick = { category ->
            var position = 0
            when (category.name) {
                resources.getString(R.string.g_chair) -> position = 1
                resources.getString(R.string.g_cupboard) -> position = 2
                resources.getString(R.string.g_table) -> position = 3
                resources.getString(R.string.g_accessory) -> position = 4
                resources.getString(R.string.g_furniture) -> position = 5
            }

            val bundle = Bundle()
            bundle.putInt("position", position)
//            findNavController().navigate(R.id.action_searchFragment_to_homeFragment, bundle)
        }
    }

    private fun onCancelTvClick() {
        binding.tvCancel.setOnClickListener {
            searchAdapter.differ.submitList(emptyList())
            binding.edSearch.setText("")
            hideCancelTv()
        }
    }

    private fun onSearchTextClick() {
        searchAdapter.onItemClick = { product ->
            val bundle = Bundle()
            bundle.putParcelable("product", product)

            /**
             * Hide the keyboard
             */

            val imm =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm!!.hideSoftInputFromWindow(requireView().windowToken, 0)

            findNavController().navigate(
                R.id.action_searchFragment_to_productPreviewFragment2,
                bundle
            )

        }
    }

    private fun setupSearchRecyclerView() {
        searchAdapter = SearchRecyclerAdapter()
        binding.rvSearch.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setupCategoryRecyclerView() {
        categoriesAdapter = CategoriesRecyclerAdapter()
        binding.rvCategories.apply {
            adapter = categoriesAdapter
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            addItemDecoration(VerticalSpacingItemDecorator(40))
        }
    }

    private fun observeCategories() {
        viewModel.categories.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Loading -> {
                    showCategoriesLoading()
                    return@Observer
                }

                is Resource.Success -> {
                    hideCategoriesLoading()
                    val categories = response.data
                    categoriesAdapter.differ.submitList(categories?.toList())
                    return@Observer
                }

                is Resource.Error -> {
                    hideCategoriesLoading()
                    Log.e(TAG, response.message.toString())
                    return@Observer
                }
            }
        })
    }

    private fun hideCategoriesLoading() {
        binding.progressbarCategories.visibility = View.GONE

    }

    private fun showCategoriesLoading() {
        binding.progressbarCategories.visibility = View.VISIBLE

    }


    private fun observeSearch() {
        viewModel.search.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Loading -> {
                    Log.d("test", "Loading")
                    return@Observer
                }

                is Resource.Success -> {
                    val products = response.data
                    searchAdapter.differ.submitList(products)
                    showChancelTv()
                    return@Observer
                }

                is Resource.Error -> {
                    Log.e(TAG, response.message.toString())
                    showChancelTv()
                    return@Observer
                }
            }
        })
    }

    var job: Job? = null
    private fun searchProducts() {
        binding.edSearch.addTextChangedListener { query ->
            val queryTrim = query.toString().trim()
            if (queryTrim.isNotEmpty()) {
                val searchQuery = query.toString().substring(0, 1).toUpperCase()
                    .plus(query.toString().substring(1))
                job?.cancel()
                job = CoroutineScope(Dispatchers.IO).launch {
                    delay(500L)
                    viewModel.searchProducts(searchQuery)
                }
            } else {
                searchAdapter.differ.submitList(emptyList())
                hideCancelTv()
            }
        }


    }

    private fun showChancelTv() {
        binding.tvCancel.visibility = View.VISIBLE
        binding.imgMic.visibility = View.GONE
        binding.imgScan.visibility = View.GONE
        binding.fragmeMicrohpone.visibility = View.GONE
        binding.frameScan.visibility = View.GONE

    }

    private fun hideCancelTv() {
        binding.tvCancel.visibility = View.GONE
        binding.imgMic.visibility = View.VISIBLE
        binding.imgScan.visibility = View.VISIBLE
        binding.fragmeMicrohpone.visibility = View.VISIBLE
        binding.frameScan.visibility = View.VISIBLE
    }

    private fun onHomeClick() {
        val btm = activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation)
        btm?.menu?.getItem(0)?.setOnMenuItemClickListener {
            activity?.onBackPressed()
            true
        }
    }

    private fun showKeyboardAutomatically() {
        inputMethodManger =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManger.toggleSoftInput(
            InputMethodManager.SHOW_FORCED,
            InputMethodManager.HIDE_IMPLICIT_ONLY
        )

        binding.edSearch.requestFocus()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.edSearch.clearFocus()
    }

    override fun onResume() {
        super.onResume()

        val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav?.visibility = View.VISIBLE
    }

}