package com.example.kleine.fragments.shopping

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kleine.R
import com.example.kleine.SpacingDecorator.HorizantalSpacingItemDecorator
import com.example.kleine.adapters.recyclerview.BillingProductsAdapter
import com.example.kleine.adapters.recyclerview.ShippingAddressesAdapter
import com.example.kleine.databinding.FragmentBillingBinding
import com.example.kleine.model.Address
import com.example.kleine.resource.Resource
import com.example.kleine.util.Constants.Companion.ORDER_FAILED_FLAG
import com.example.kleine.util.Constants.Companion.ORDER_SUCCESS_FLAG
import com.example.kleine.util.Constants.Companion.UPDATE_ADDRESS_FLAG
import com.example.kleine.viewmodel.billing.BillingViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class BillingFragment : Fragment() {
    val args by navArgs<BillingFragmentArgs>()
    val TAG = "BillingFragment"
    private lateinit var binding: FragmentBillingBinding
    private lateinit var shippingAddressesAdapter: ShippingAddressesAdapter
    private lateinit var viewModel: BillingViewModel
    private lateinit var cartProductsAdapter:BillingProductsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this)[BillingViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBillingBinding.inflate(inflater)
        val bottomNavigation = activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation?.visibility = View.INVISIBLE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val price = args.price

        if (price == "null") {
            binding.apply {
                linear.visibility = View.GONE
                btnPlaceOlder.visibility = View.GONE
                line2.visibility = View.GONE
                rvProducts.visibility = View.GONE
                line3.visibility = View.GONE
            }
        } else {
            binding.apply {
                linear.visibility = View.VISIBLE
                btnPlaceOlder.visibility = View.VISIBLE
                line2.visibility = View.VISIBLE
                btnPlaceOlder.visibility = View.VISIBLE
                tvTotalprice.text = price
                rvProducts.visibility = View.VISIBLE
                setupProductsRecyclerview()
                cartProductsAdapter.differ.submitList(args.products?.products)
            }
        }

        onAddAddressImgClick()
        onImgCloseClick()
        setupAddressesRecyclerview()
        observeAddresses()
        onShippingItemClick()
        onPlaceOrderClick()

        observePlaceOrder()
    }

    private fun observePlaceOrder() {
        viewModel.placeOrder.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                    is Resource.Loading -> {
                        showPlaceOrderLoading()
                        return@Observer
                    }

                    is Resource.Success -> {
                        hidePlaceOrderLoading()
                        val bundle = Bundle()
                        bundle.putString("order_completion_flag", ORDER_SUCCESS_FLAG)
                        bundle.putString("orderNumber",response.data?.id)
                        bundle.putParcelable("order",response.data)
                        findNavController().navigate(R.id.action_billingFragment_to_orderCompletion,bundle)
                        return@Observer
                    }

                    is Resource.Error -> {
                        hidePlaceOrderLoading()
                        Log.e(TAG,response.message.toString())
                        val bundle = Bundle()
                        bundle.putString("order_completion_flag", ORDER_FAILED_FLAG)
                        findNavController().navigate(R.id.action_billingFragment_to_orderCompletion,bundle)
                        return@Observer
                    }
                }
        })
    }

    private fun hidePlaceOrderLoading() {
        binding.apply {
            progressbarPlaceOrder.visibility = View.GONE
            btnPlaceOlder.visibility = View.VISIBLE
        }

    }

    private fun showPlaceOrderLoading() {
        binding.apply {
            progressbarPlaceOrder.visibility = View.VISIBLE
            btnPlaceOlder.visibility = View.INVISIBLE
        }
    }

    private fun setupProductsRecyclerview() {
        cartProductsAdapter = BillingProductsAdapter()
        binding.rvProducts.apply {
            adapter = cartProductsAdapter
            layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            addItemDecoration(HorizantalSpacingItemDecorator(23))
        }
    }

    private fun onPlaceOrderClick() {
        binding.btnPlaceOlder.setOnClickListener {
            if (selectedAddress == null) {
                binding.tvSelectAddressError.visibility = View.VISIBLE
                return@setOnClickListener
            }

            setupAlertDialog()


        }
    }

    private fun setupAlertDialog() {
        val alertDialog = AlertDialog.Builder(context).create()
        val view = LayoutInflater.from(context).inflate(R.layout.delete_alert_dialog,null,false)
        alertDialog.setView(view)
        val title = view.findViewById<TextView>(R.id.tv_delete_item)
        val message = view.findViewById<TextView>(R.id.tv_delete_message)
        val btnConfirm = view.findViewById<Button>(R.id.btn_yes)
        val btnCancel = view.findViewById<Button>(R.id.btn_no)
        title.text = resources.getText(R.string.g_place_order)
        message.text = resources.getText(R.string.g_place_order_confirmation)
        btnConfirm.text = resources.getText(R.string.g_confirm)
        btnCancel.text = resources.getText(R.string.g_cancel)


        btnConfirm.setOnClickListener {
            viewModel.placeOrder(args.products!!.products,selectedAddress!!,args.price!!)
            alertDialog.dismiss()
        }

        btnCancel.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    private var selectedAddress: Address? = null
    private fun onShippingItemClick() {
        shippingAddressesAdapter.onBtnClick = { address ->
            if(args.clickFlag == UPDATE_ADDRESS_FLAG) {
                val bundle = Bundle()
                bundle.putParcelable("address", address)
                selectedAddress = address
                findNavController().navigate(R.id.action_billingFragment_to_addressFragment, bundle)

            }else{
                selectedAddress = address
                binding.tvSelectAddressError.visibility = View.GONE
            }
        }
    }

    private fun observeAddresses() {
        viewModel.addresses.observe(viewLifecycleOwner, Observer { response ->
            if (response.data == null)
                hideLoading()
            else
                when (response) {
                    is Resource.Loading -> {
                        showLoading()
                        return@Observer
                    }

                    is Resource.Success -> {
                        hideLoading()
                        shippingAddressesAdapter.differ.submitList(response.data.toList())
                        return@Observer
                    }

                    is Resource.Error -> {
                        hideLoading()
                        Log.e(TAG, response.message.toString())
                        Toast.makeText(activity, "Error occurred", Toast.LENGTH_SHORT).show()
                        return@Observer
                    }
                }
        })
    }

    private fun hideLoading() {
        binding.progressbarAddresses.visibility = View.GONE
    }

    private fun showLoading() {
        binding.progressbarAddresses.visibility = View.VISIBLE
    }

    private fun setupAddressesRecyclerview() {
        shippingAddressesAdapter = ShippingAddressesAdapter(args.clickFlag)
        binding.rvAdresses.apply {
            adapter = shippingAddressesAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(HorizantalSpacingItemDecorator(23))
        }
    }

    private fun onImgCloseClick() {
        binding.imgCloseBilling.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun onAddAddressImgClick() {
        binding.imgAddAddress.setOnClickListener {
            findNavController().navigate(R.id.action_billingFragment_to_addressFragment)
        }
    }


}