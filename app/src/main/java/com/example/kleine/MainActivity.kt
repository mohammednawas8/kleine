package com.example.kleine

import android.icu.lang.UCharacter.GraphemeClusterBreak.L
import android.os.Build.VERSION_CODES.M
import android.os.Build.VERSION_CODES.S
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.kleine.firebaseDatabase.FirebaseDb
import com.example.kleine.model.Products
import com.example.kleine.util.Constants
import com.example.kleine.util.Constants.Companion.BEST_DEALS
import com.example.kleine.util.Constants.Companion.BLACK
import com.example.kleine.util.Constants.Companion.CLOTHES
import com.example.kleine.util.Constants.Companion.COLORS
import com.example.kleine.util.Constants.Companion.CUPBOARD_CATEGORY
import com.example.kleine.util.Constants.Companion.GREEN
import com.example.kleine.util.Constants.Companion.IMAGES
import com.example.kleine.util.Constants.Companion.ORANGE
import com.example.kleine.util.Constants.Companion.PRODUCTS_COLLECTION
import com.example.kleine.util.Constants.Companion.RED
import com.example.kleine.util.Constants.Companion.SIZES
import com.example.kleine.util.Constants.Companion.XLARGE
import com.example.kleine.viewmodel.KleineViewModel
import com.example.kleine.viewmodel.ViewModelProviderFactory
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    lateinit var viewModel:KleineViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


//        saveNewProduct()

        supportActionBar?.hide()
        val firebaseDb = FirebaseDb()
        val viewModelFactory = ViewModelProviderFactory(firebaseDb)
        viewModel = ViewModelProvider(this,viewModelFactory)[KleineViewModel::class.java]

    }

//    private fun saveNewProduct() {
//
//        val title = "Special Shirt's"
//        val description = null
//        val category = BEST_DEALS
//        val oldPrice = "2000"
//        val price = "1600"
//        val seller = "mo mart"
//
//        val images = HashMap<String,Any>()
//        val imagesList = listOf(
//            "https://firebasestorage.googleapis.com/v0/b/kleine-6e51a.appspot.com/o/products%2FZnDRiKZR2Ucg46xATKBA%2FImput%20Image.png?alt=media&token=c4ed7492-526b-48a6-99e1-9d2b7f97aeba",
//            "https://firebasestorage.googleapis.com/v0/b/kleine-6e51a.appspot.com/o/products%2FZnDRiKZR2Ucg46xATKBA%2FImput%20Image.png?alt=media&token=c4ed7492-526b-48a6-99e1-9d2b7f97aeba",
//            "https://firebasestorage.googleapis.com/v0/b/kleine-6e51a.appspot.com/o/products%2FZnDRiKZR2Ucg46xATKBA%2FImput%20Image.png?alt=media&token=c4ed7492-526b-48a6-99e1-9d2b7f97aeba"
//        )
//
//        images.put(IMAGES,imagesList.toList())
//
//        val colors = HashMap<String,Any>()
//        val colorsList = listOf<String>(
//            GREEN,
//            RED,
//            BLACK,
//            ORANGE
//        )
//        colors.put(COLORS, colorsList.toList())
//
//        val sizes = HashMap<String,Any>()
//        val sizesList = listOf(
//            S,
//            M,
//            L,
//            XLARGE
//        )
//        sizes.put(SIZES,sizesList.toList())
//
//        val prodcut = Products(title, description, category, price,oldPrice, seller, images, colors, sizes)
//
//        Firebase.firestore.collection(PRODUCTS_COLLECTION)
//            .document(
//                    "Ff7YVgeGV7chY0dcW3EW")
//            .set(prodcut)
//
//
//    }
}