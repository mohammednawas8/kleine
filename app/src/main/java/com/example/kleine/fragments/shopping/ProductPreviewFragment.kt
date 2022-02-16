package com.example.kleine.fragments.shopping

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kleine.R
import com.example.kleine.SpacingDecorator.SpacingItemDecorator
import com.example.kleine.adapters.recyclerview.ColorsAndSizesAdapter
import com.example.kleine.adapters.viewpager.ViewPager2Images
import com.example.kleine.databinding.FragmentProductPreviewBinding
import com.example.kleine.model.Product
import com.example.kleine.util.Constants.Companion.COLORS
import com.example.kleine.util.Constants.Companion.COLORS_TYPE
import com.example.kleine.util.Constants.Companion.IMAGES
import com.example.kleine.util.Constants.Companion.SIZES
import com.example.kleine.util.Constants.Companion.SIZES_TYPE
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.github.vejei.viewpagerindicator.indicator.CircleIndicator

class ProductPreviewFragment : Fragment() {

    val args by navArgs<ProductPreviewFragmentArgs>()
    private lateinit var binding: FragmentProductPreviewBinding
    private lateinit var colorsAdapter: ColorsAndSizesAdapter
    private lateinit var sizesAdapter: ColorsAndSizesAdapter
    private lateinit var viewPagerAdapter:ViewPager2Images

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        colorsAdapter = ColorsAndSizesAdapter(COLORS_TYPE)
        sizesAdapter = ColorsAndSizesAdapter(SIZES_TYPE)
        viewPagerAdapter = ViewPager2Images()

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