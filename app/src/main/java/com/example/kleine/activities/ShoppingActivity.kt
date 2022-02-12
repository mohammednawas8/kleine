package com.example.kleine.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kleine.R

class ShoppingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping)

        supportActionBar!!.hide()
    }
}