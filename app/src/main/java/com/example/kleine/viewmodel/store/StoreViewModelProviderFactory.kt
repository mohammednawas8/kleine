package com.example.kleine.viewmodel.store

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kleine.firebaseDatabase.FirebaseDb

class StoreViewModelProviderFactory(
    val db:FirebaseDb
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return StoreViewModel(db) as T
    }
}