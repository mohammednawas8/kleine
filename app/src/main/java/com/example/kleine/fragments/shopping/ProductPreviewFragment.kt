package com.example.kleine.fragments.shopping

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.simplepass.loadingbutton.customViews.CircularProgressButton
import com.example.kleine.R
import com.example.kleine.SpacingDecorator.SpacingItemDecorator
import com.example.kleine.activities.ShoppingActivity
import com.example.kleine.adapters.recyclerview.ColorsAndSizesAdapter
import com.example.kleine.adapters.viewpager.ViewPager2Images
import com.example.kleine.databinding.FragmentProductPreviewBinding
import com.example.kleine.firebaseDatabase.FirebaseDb
import com.example.kleine.model.Product
import com.example.kleine.resource.Resource
import com.example.kleine.util.Constants.Companion.COLORS
import com.example.kleine.util.Constants.Companion.COLORS_TYPE
import com.example.kleine.util.Constants.Companion.IMAGES
import com.example.kleine.util.Constants.Companion.SIZES
import com.example.kleine.util.Constants.Companion.SIZES_TYPE
import com.example.kleine.viewmodel.shopping.ShoppingViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import io.github.vejei.viewpagerindicator.indicator.CircleIndicator

class ProductPreviewFragment : Fragment() {

    val args by navArgs<ProductPreviewFragmentArgs>()
    val TAG = "ProductPreviewFragment"

    private lateinit var binding: FragmentProductPreviewBinding
    private lateinit var colorsAdapter: ColorsAndSizesAdapter
    private lateinit var sizesAdapter: ColorsAndSizesAdapter
    private lateinit var viewPagerAdapter: ViewPager2Images
    private lateinit var viewModel: ShoppingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        colorsAdapter = ColorsAndSizesAdapter(COLORS_TYPE)
        sizesAdapter = ColorsAndSizesAdapter(SIZES_TYPE)
        viewPagerAdapter = ViewPager2Images()
        viewModel = (activity as ShoppingActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProductPreviewBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNavigation =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.visibility = View.GONE

        val product = args.product


        setupViewpager()
        setupColorsRecyclerview()
        setupSizesRecyclerview()

        setProductInformation(product)

        onImageCloseClick()
        onBtnAddToCartClick()

        observeAddToCart()

    }

    private fun observeAddToCart() {
        viewModel.addToCart.observe(viewLifecycleOwner, Observer { response ->
            val btn = binding.btnAddToCart
            when (response) {
                is Resource.Loading -> {
                    startLoading()
                    return@Observer
                }

                is Resource.Success -> {
                    stopLoading()
                    viewModel.addToCart.value = null
                    return@Observer
                }

                is Resource.Error -> {
                    btn.revertAnimation()
                    viewModel.addToCart.value = null
                    Log.e(TAG, response.message.toString())
                }
            }
        })
    }

    private fun stopLoading() {
        val btn = binding.btnAddToCart
        btn.revertAnimation()
        btn.setBackgroundColor(resources.getColor(R.color.black))
        binding.btnAddToCart.isClickable = true


    }

    private fun startLoading() {
        val btn = binding.btnAddToCart as CircularProgressButton
        btn.apply {
            spinningBarColor = resources.getColor(R.color.white)
            spinningBarWidth = resources.getDimension(R.dimen._5sdp)
            binding.btnAddToCart.isClickable = false
            startAnimation()
        }
    }

    //use viewModel later
    private fun checkIfItemInCard(onSuccess: (Boolean) -> Unit) {
        FirebaseFirestore.getInstance().collection("users")
            .document(FirebaseAuth.getInstance().currentUser!!.uid)
            .collection("cart")
            .whereEqualTo("id", args.product.id)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    startLoading()
                    val result = it.result
                    if (result!!.isEmpty)
                        onSuccess(true)
                    else
                        onSuccess(false)

                } else {
                    onSuccess(false)
                }
            }
    }

    private fun onBtnAddToCartClick() {
        checkIfItemInCard {
            if (it)
                binding.btnAddToCart.revertAnimation()
            else {
                stopLoading()
            }
        }

        binding.btnAddToCart.setOnClickListener {
            if (binding.btnAddToCart.text == resources.getText(R.string.g_added))
                Toast.makeText(
                    activity,
                    resources.getText(R.string.g_already_added),
                    Toast.LENGTH_SHORT
                ).show()
            else {
                val product = args.product
                product.quantity = 1
                viewModel.addProductToCart(product)
            }

        }

    }

    private fun onImageCloseClick() {
        binding.imgClose.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun setProductInformation(product: Product) {
        val imagesList = product.images!![IMAGES] as List<String>
        val colors = product.colors!![COLORS] as List<String>
        val sizes = product.sizes!![SIZES] as List<String>
        binding.apply {
            viewPagerAdapter.differ.submitList(imagesList)
            colorsAdapter.differ.submitList(colors.toList())
            sizesAdapter.differ.submitList(sizes)
            tvProductName.text = product.title
            tvProductDescription.text = product.description
            tvProductPrice.text = "$${product.price}"
        }
    }

    private fun setupSizesRecyclerview() {
        binding.rvSizes.apply {
            adapter = sizesAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(SpacingItemDecorator(45))
        }
    }

    private fun setupColorsRecyclerview() {
        binding.rvColors.apply {
            adapter = colorsAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(SpacingItemDecorator(45))
        }
    }

    private fun setupViewpager() {
        binding.viewpager2Images.adapter = viewPagerAdapter
        binding.circleIndicator.setWithViewPager2(binding.viewpager2Images)
        binding.circleIndicator.itemCount = (args.product.images?.get(IMAGES) as List<String>).size
        binding.circleIndicator.setAnimationMode(CircleIndicator.AnimationMode.SLIDE)
    }

}