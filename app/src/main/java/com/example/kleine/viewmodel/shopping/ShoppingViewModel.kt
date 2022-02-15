package com.example.kleine.viewmodel.shopping

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kleine.firebaseDatabase.FirebaseDb
import com.example.kleine.model.Category
import com.example.kleine.model.Product
import com.example.kleine.resource.Resource
import com.example.kleine.util.Constants.Companion.CUPBOARD_CATEGORY
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.math.sign

class ShoppingViewModel(
    private val firebaseDatabase: FirebaseDb
) : ViewModel() {
    private val TAG = "ShoppingViewModel"

    val clothes = MutableLiveData<List<Product>>()
    val emptyClothes = MutableLiveData<Boolean>()
    val bestDeals = MutableLiveData<List<Product>>()
    val emptyBestDeals = MutableLiveData<Boolean>()
    val chairs = MutableLiveData<List<Product>>()
    val mostOrderedCupboard = MutableLiveData<List<Product>>()
    val cupboard = MutableLiveData<List<Product>>()

    private var chairsPagingPage: Long = 10
    private var clothesPaging: Long = 5
    private var bestDealsPaging: Long = 5
    private var cupboardPaging: Long = 10

    private var mostOrderCupboardPaging: Long = 5

    init {
        getClothesProducts()
        getBestDealsProduct()
        getChairs()
        getCupboardsByOrders(5)
        getCupboardProduct(10)
    }

    fun getClothesProducts() =
        firebaseDatabase.getClothesProducts(clothesPaging).addOnCompleteListener {
            if (it.isSuccessful) {
                val documents = it.result
                if (!documents!!.isEmpty) {
                    val productsList = documents.toObjects(Product::class.java)
                    clothes.postValue(productsList)
                    clothesPaging += 5
                } else
                    emptyClothes.postValue(true)

            } else
                Log.e(TAG, it.exception.toString())

        }

    fun getBestDealsProduct() =
        firebaseDatabase.getBestDealsProducts(bestDealsPaging).addOnCompleteListener {
            if (it.isSuccessful) {
                val documents = it.result
                if (!documents!!.isEmpty) {
                    val productsList = documents.toObjects(Product::class.java)
                    bestDeals.postValue(productsList)
                    bestDealsPaging += 5
                } else
                    emptyBestDeals.postValue(true)

            } else
                Log.e(TAG, it.exception.toString())
        }

    fun getChairs() = firebaseDatabase.getChairs(chairsPagingPage).addOnCompleteListener {
        if (it.isSuccessful) {
            val documents = it.result
            if (!documents!!.isEmpty) {
                val productsList = documents.toObjects(Product::class.java)
                chairs.postValue(productsList)
                chairsPagingPage += 10

            }
        } else {
            Log.e(TAG, it.exception.toString())
        }

    }


    fun getCupboardsByOrders(size: Int) =
        shouldPaging(CUPBOARD_CATEGORY, size) {
            if (it) {
                firebaseDatabase.getMostOrderedCupboard(mostOrderCupboardPaging).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.d("paging",cupboardPaging.toString())
                        val documents = it.result
                        if (!documents!!.isEmpty) {
                            val productsList = documents.toObjects(Product::class.java)
                            mostOrderedCupboard.postValue(productsList)
                            cupboardPaging += 5
                        }

                    } else
                        Log.d(TAG, it.exception.toString())
                }
            }
        }

    fun getCupboardProduct(size: Int) =
        shouldPaging(CUPBOARD_CATEGORY, size) {
            if (it) {
                firebaseDatabase.getCupboards(cupboardPaging).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val documents = it.result
                        if (!documents!!.isEmpty) {
                            val productsList = documents.toObjects(Product::class.java)
                            cupboard.postValue(productsList)
                            cupboardPaging += 10
                        }

                    } else
                        Log.d(TAG, it.exception.toString())
                }
            }
        }

    private fun shouldPaging(category: String, listSize: Int, onSuccess: (Boolean) -> Unit) {
        FirebaseFirestore.getInstance()
            .collection("categories")
            .whereEqualTo("name", category).get().addOnSuccessListener {
                val tempCategory = it.toObjects(Category::class.java)
                val products = tempCategory[0].products
                Log.d("test", " prodcuts ${tempCategory[0].products}, size $listSize")
                if (listSize == products)
                    onSuccess(false)
                else
                    onSuccess(true)
            }
    }
}