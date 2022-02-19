package com.example.kleine.viewmodel.shopping.cart

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kleine.firebaseDatabase.FirebaseDb
import com.example.kleine.model.CartProduct
import com.example.kleine.resource.Resource

class CartViewModel(
) : ViewModel() {
    val cartProducts = MutableLiveData<Resource<List<CartProduct>>>()
    val cartItemsCount = MutableLiveData<Resource<Int>>()
    var firebaseDb: FirebaseDb = FirebaseDb()

    init {
        getItemsInCart()
    }

    private fun getItemsInCart() {
        cartProducts.postValue(Resource.Loading())
        cartItemsCount.postValue(Resource.Loading())

        firebaseDb.getItemsInCart().addSnapshotListener { value, error ->
            if (error != null) {
                cartProducts.postValue(Resource.Error(error.message))
                cartItemsCount.postValue(Resource.Error(error.message))
            }
            else {
                val products = value!!.toObjects(CartProduct::class.java)
                cartProducts.postValue(Resource.Success(products))
                cartItemsCount.postValue(Resource.Success(products.size))
            }
        }
    }
}