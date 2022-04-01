package com.example.kleine.viewmodel.billing

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kleine.firebaseDatabase.FirebaseDb
import com.example.kleine.model.Address
import com.example.kleine.model.CartProduct
import com.example.kleine.model.Order
import com.example.kleine.resource.Resource
import com.example.kleine.util.Constants.Companion.ORDER_PLACED_STATE
import java.util.*
import kotlin.random.Random

//change this later and add view model provider factory

class BillingViewModel : ViewModel() {
    val placeOrder = MutableLiveData<Resource<Order>>()
    val firebaseDatabase = FirebaseDb()
    val addresses = MutableLiveData<Resource<List<Address>>>()

    init {
        getShippingAddresses()
    }

    private fun getShippingAddresses() {
        addresses.postValue(Resource.Loading())
        firebaseDatabase.getAddresses()?.addSnapshotListener { value, error ->
            if (error != null) {
                addresses.postValue(Resource.Error(error.toString()))
                return@addSnapshotListener
            }
            if (!value!!.isEmpty) {
                val addressesList = value.toObjects(Address::class.java)
                addresses.postValue(Resource.Success(addressesList))
            }
        }
    }

    fun placeOrder(products:List<CartProduct>,address: Address,price:String){
        placeOrder.postValue(Resource.Loading())
        val id = Random.nextInt(9999999)
        val date = Calendar.getInstance().time
        val order = Order(id.toString(),date,price,ORDER_PLACED_STATE)

        firebaseDatabase.placeOrder(products, address, order).addOnCompleteListener {
            if(it.isSuccessful)
                placeOrder.postValue(Resource.Success(order))
            else
                placeOrder.postValue(Resource.Error(it.exception.toString()))
        }
    }
}
