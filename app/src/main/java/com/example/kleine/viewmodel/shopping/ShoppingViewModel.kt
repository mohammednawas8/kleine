package com.example.kleine.viewmodel.shopping

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kleine.firebaseDatabase.FirebaseDb
import com.example.kleine.model.Product

class ShoppingViewModel(
    private val firebaseDatabase:FirebaseDb
) : ViewModel() {
    val clothes = MutableLiveData<List<Product>>()
    val bestDeals = MutableLiveData<List<Product>>()
    val chairs = MutableLiveData<List<Product>>()

    init {
        getClothesProducts()
        getBestDealsProduct()
        getChairs()
    }

    private fun getClothesProducts() = firebaseDatabase.getClothesProducts().addOnSuccessListener { documents->
        if(!documents.isEmpty){
            val productsList = documents.toObjects(Product::class.java)
            clothes.postValue(productsList)
        }
    }

    private fun getBestDealsProduct() = firebaseDatabase.getBestDealsProducts().addOnSuccessListener { documents->
        if(!documents.isEmpty){
            val productsList = documents.toObjects(Product::class.java)
            bestDeals.postValue(productsList)
        }
    }

    private fun getChairs() = firebaseDatabase.getChairs().addOnSuccessListener { documents->
        if(!documents.isEmpty){
            val productsList = documents.toObjects(Product::class.java)
            chairs.postValue(productsList)
        }
    }


}