package com.example.kleine.fragments.shopping

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.kleine.R
import com.example.kleine.activities.ShoppingActivity
import com.example.kleine.databinding.FragmentAddressBinding
import com.example.kleine.model.Address
import com.example.kleine.resource.Resource
import com.example.kleine.viewmodel.shopping.ShoppingViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView


class AddressFragment : Fragment() {
    val args by navArgs<AddressFragmentArgs>()
    val TAG = "AddressFragment"
    private lateinit var binding: FragmentAddressBinding
    private lateinit var viewModel: ShoppingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (activity as ShoppingActivity).viewModel

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddressBinding.inflate(inflater)
        val bottomNavigation = activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation?.visibility = View.INVISIBLE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val address = args.address
        if (address == null) {
            onSaveClick()
            binding.btnDelete.visibility = View.GONE
            observeAddAddress()
        } else {
            setInformation(address)
            updateAddress(address)
            observeUpdateAddress()
            onDeleteClick(address)
            observeDeleteAddress()
        }

        onImgCloseClick()




    }

    private fun observeDeleteAddress() {
        viewModel.deleteAddress.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Loading -> {
                    showLoading()
                    return@Observer
                }

                is Resource.Success -> {
                    hideLoading()
                    findNavController().navigateUp()
                    viewModel.deleteAddress.postValue(null)
                    return@Observer
                }

                is Resource.Error -> {
                    hideLoading()
                    Log.e(TAG, response.message.toString())
                    Toast.makeText(activity, "Error occurred", Toast.LENGTH_SHORT).show()
                    return@Observer
                }
            }
        })    }

    private fun onDeleteClick(address: Address) {
        binding.btnDelete.setOnClickListener {
            viewModel.deleteAddress(address)
        }
    }

    private fun observeUpdateAddress() {
        viewModel.updateAddress.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Loading -> {
                    showLoading()
                    return@Observer
                }

                is Resource.Success -> {
                    hideLoading()
                    findNavController().navigateUp()
                    viewModel.updateAddress.postValue(null)
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

    private fun updateAddress(oldAddress: Address) {
        binding.btnAddNewAddress.setOnClickListener {
            binding.apply {
                val title = edAddressTitle.text.toString()
                val fullName = edFullName.text.toString()
                val street = edStreet.text.toString()
                val phone = edPhone.text.toString()
                val city = edCity.text.toString()
                val state = edState.text.toString()

                val newAddress = Address(title, fullName, street, phone, city, state)
                viewModel.updateAddress(oldAddress,newAddress)
            }
        }
    }

    private fun setInformation(address: Address) {
        binding.apply {
            edAddressTitle.setText(address.addressTitle)
            edFullName.setText(address.fullName)
            edPhone.setText(address.phone)
            edCity.setText(address.city)
            edState.setText(address.state)
            edStreet.setText(address.street)

            btnAddNewAddress.text = resources.getText(R.string.g_update)
        }
    }

    private fun observeAddAddress() {
        viewModel.addAddress.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Loading -> {
                    showLoading()
                    return@Observer
                }

                is Resource.Success -> {
                    hideLoading()
                    findNavController().navigateUp()
                    viewModel.addAddress.postValue(null)
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
        binding.apply {
            btnAddNewAddress.visibility = View.VISIBLE
            btnDelete.visibility = View.VISIBLE
            progressbarAddress.visibility = View.INVISIBLE
        }
    }

    private fun showLoading() {
        binding.apply {
            btnAddNewAddress.visibility = View.INVISIBLE
            btnDelete.visibility = View.INVISIBLE
            progressbarAddress.visibility = View.VISIBLE
        }
    }

    private fun onSaveClick() {
        binding.apply {
            btnAddNewAddress.setOnClickListener {
                val title = edAddressTitle.text.toString()
                val fullName = edFullName.text.toString()
                val street = edStreet.text.toString()
                val phone = edPhone.text.toString()
                val city = edCity.text.toString()
                val state = edState.text.toString()

                if (title.isEmpty() || fullName.isEmpty() || street.isEmpty() ||
                    phone.isEmpty() || city.isEmpty() || state.isEmpty()
                ) {
                    Toast.makeText(
                        activity,
                        "Make sure you filled all requirements",
                        Toast.LENGTH_LONG
                    ).show()
                    return@setOnClickListener
                }

                val address = Address(title, fullName, street, phone, city, state)
                viewModel.saveAddress(address)
            }
        }
    }

    private fun onImgCloseClick() {
        binding.imgAddressClose.setOnClickListener {
            findNavController().navigateUp()
        }
    }

}