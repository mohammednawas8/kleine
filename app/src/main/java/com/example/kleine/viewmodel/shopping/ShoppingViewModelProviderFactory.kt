package com.example.kleine.viewmodel.shopping

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kleine.firebaseDatabase.FirebaseDb

class ShoppingViewModelProviderFactory(
    val db:FirebaseDb
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ShoppingViewModel(db) as T
    }
}