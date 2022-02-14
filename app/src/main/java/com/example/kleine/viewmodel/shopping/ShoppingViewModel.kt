package com.example.kleine.viewmodel.shopping

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kleine.firebaseDatabase.FirebaseDb
import com.example.kleine.model.Product

class ShoppingViewModel(
    private val firebaseDatabase: FirebaseDb
) : ViewModel() {
    val clothes = MutableLiveData<List<Product>>()
    val emptyClothes = MutableLiveData<Boolean>()
    val bestDeals = MutableLiveData<List<Product>>()
    val emptyBestDeals = MutableLiveData<Boolean>()
    val chairs = MutableLiveData<List<Product>>()
    val mostCupboardOrdered = MutableLiveData<List<Product>>()

    private var chairsPagingPage: Long = 10
    private var clothesPaging: Long = 5
    private var bestDealsPaging: Long = 5

    private var mostOrderCupboardPaging: Long = 5

    init {
        getClothesProducts()
        getBestDealsProduct()
        getChairs()
        getCupboardsByOrders()

    }

    fun getClothesProducts() =
        firebaseDatabase.getClothesProducts(clothesPaging).addOnSuccessListener { documents ->
            if (!documents.isEmpty) {
                val productsList = documents.toObjects(Product::class.java)
                clothes.postValue(productsList)
                clothesPaging += 5
            } else {
                emptyClothes.postValue(true)
            }
        }

    fun getBestDealsProduct() =
        firebaseDatabase.getBestDealsProducts(bestDealsPaging).addOnSuccessListener { documents ->
            if (!documents.isEmpty) {
                val productsList = documents.toObjects(Product::class.java)
                bestDeals.postValue(productsList)
                bestDealsPaging += 5
            } else {
                emptyBestDeals.postValue(true)
            }
        }

    fun getChairs() =
        firebaseDatabase.getChairs(chairsPagingPage).addOnSuccessListener { documents ->
            if (!documents.isEmpty) {
                val productsList = documents.toObjects(Product::class.java)
                chairs.postValue(productsList)
                chairsPagingPage += 10

            }
        }

    fun getCupboardsByOrders() =
        firebaseDatabase.getMostOrderedCupboard(mostOrderCupboardPaging).addOnSuccessListener { documents ->
        if (!documents.isEmpty) {
            val productsList = documents.toObjects(Product::class.java)
            mostCupboardOrdered.postValue(productsList)
            chairsPagingPage += 5

        }
    }
}