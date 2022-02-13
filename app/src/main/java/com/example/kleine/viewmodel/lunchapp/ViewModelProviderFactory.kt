package com.example.kleine.viewmodel.lunchapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kleine.firebaseDatabase.FirebaseDb

class ViewModelProviderFactory(
    private val firebaseDb: FirebaseDb
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return KleineViewModel(firebaseDb) as T
    }
}