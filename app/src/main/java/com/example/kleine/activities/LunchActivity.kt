package com.example.kleine.activities

import android.content.Intent
import android.icu.lang.UCharacter.GraphemeClusterBreak.L
import android.os.Build.VERSION_CODES.M
import android.os.Build.VERSION_CODES.S
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.kleine.R
import com.example.kleine.firebaseDatabase.FirebaseDb
import com.example.kleine.model.Product
import com.example.kleine.util.Constants
import com.example.kleine.util.Constants.Companion.ACCESSORY_CATEGORY
import com.example.kleine.util.Constants.Companion.BLACK
import com.example.kleine.util.Constants.Companion.CHAIR_CATEGORY
import com.example.kleine.util.Constants.Companion.COLORS
import com.example.kleine.util.Constants.Companion.CUPBOARD_CATEGORY
import com.example.kleine.util.Constants.Companion.GREEN
import com.example.kleine.util.Constants.Companion.IMAGES
import com.example.kleine.util.Constants.Companion.LARGE
import com.example.kleine.util.Constants.Companion.MEDIUM
import com.example.kleine.util.Constants.Companion.ORANGE
import com.example.kleine.util.Constants.Companion.ORDERS
import com.example.kleine.util.Constants.Companion.PRODUCTS_COLLECTION
import com.example.kleine.util.Constants.Companion.RED
import com.example.kleine.util.Constants.Companion.SIZES
import com.example.kleine.util.Constants.Companion.TABLES_CATEGORY
import com.example.kleine.util.Constants.Companion.XLARGE
import com.example.kleine.viewmodel.lunchapp.KleineViewModel
import com.example.kleine.viewmodel.lunchapp.ViewModelProviderFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LunchActivity : AppCompatActivity() {
    val viewModel by lazy {
        val firebaseDb = FirebaseDb()
        val viewModelFactory = ViewModelProviderFactory(firebaseDb)
        ViewModelProvider(this,viewModelFactory)[KleineViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lunch)

        //user viewmodel later
        if(FirebaseAuth.getInstance().currentUser!=null)
        {
            val intent = Intent(this,ShoppingActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        supportActionBar?.hide()


//        val random = Random.nextInt(from = 10000, until = 99999)


//        saveNewProduct()
    }

    private fun saveNewProduct() {

        val title = "Ronda 40cm 3 Light Pendant Slate Grey"
        val description = "Modern home lighting 2022 made in Palestine with high lighting quality"

            val category = ACCESSORY_CATEGORY
        val price = "50"
        val newPrice = "35"
        val seller = "ps mart"
        val orders = 120

        val images = HashMap<String,Any>()
        val imagesList = listOf(
            "https://www.nottinghamlightingcentre.co.uk/app/uploads/2019/04/RON1039_0.jpg",
            "https://www.nottinghamlightingcentre.co.uk/app/uploads/2019/04/RON1039_0.jpg"
        )

        images.put(IMAGES,imagesList.toList())

        val colors = HashMap<String,Any>()
        val colorsList = listOf<String>(
            "#292929",
            "#d1d1d1",
        )
        colors.put(COLORS, colorsList.toList())

        val sizes = HashMap<String,Any>()
        val sizesList = listOf(
            "40cm"
        )
        sizes.put(SIZES,sizesList.toList())

        val prodcut = Product(123071,title, description, category, newPrice,price, seller, images, colors, sizes,orders)

        Firebase.firestore.collection(PRODUCTS_COLLECTION)
            .document()
            .set(prodcut)
    }
}