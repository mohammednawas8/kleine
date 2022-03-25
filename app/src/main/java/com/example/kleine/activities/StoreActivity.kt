package com.example.kleine.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kleine.databinding.ActivityStoreBinding

class StoreActivity : AppCompatActivity() {

    private lateinit var binding:ActivityStoreBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoreBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }




}