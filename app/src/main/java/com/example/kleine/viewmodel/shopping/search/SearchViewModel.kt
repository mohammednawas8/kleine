package com.example.kleine.viewmodel.shopping.search

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kleine.firebaseDatabase.FirebaseDb
import com.example.kleine.model.Category
import com.example.kleine.model.Product
import com.example.kleine.resource.Resource

class SearchViewModel : ViewModel() {
    val search = MutableLiveData<Resource<List<Product>>>()
    val categories = MutableLiveData<Resource<List<Category>>>()
    private var firebaseDatabase = FirebaseDb()

    init {
        getCategories()
    }

    fun searchProducts(searchQuery: String) {
        search.postValue(Resource.Loading())
        firebaseDatabase.searchProducts(searchQuery).addOnCompleteListener {
            if (it.isSuccessful) {
                val productsList = it.result!!.toObjects(Product::class.java)
                search.postValue(Resource.Success(productsList))

            } else
                search.postValue(Resource.Error(it.exception.toString()))

        }
    }

    fun getCategories() {
        categories.postValue(Resource.Loading())
        firebaseDatabase.getCategories().addOnCompleteListener {
            if (it.isSuccessful) {
                val categoriesList = it.result!!.toObjects(Category::class.java)
                categories.postValue(Resource.Success(categoriesList))
            } else
                categories.postValue(Resource.Error(it.exception.toString()))
        }


    }
}