package com.example.kleine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kleine.firebaseDatabase.FirebaseDb
import com.example.kleine.viewmodel.KleineViewModel
import com.example.kleine.viewmodel.ViewModelProviderFactory

class MainActivity : AppCompatActivity() {
    lateinit var viewModel:KleineViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()
        val firebaseDb = FirebaseDb()
        val viewModelFactory = ViewModelProviderFactory(firebaseDb)
        viewModel = ViewModelProvider(this,viewModelFactory)[KleineViewModel::class.java]

    }
}