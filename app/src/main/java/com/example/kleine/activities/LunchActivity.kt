package com.example.kleine.activities

import android.content.Intent
import android.icu.lang.UCharacter.GraphemeClusterBreak.L
import android.os.Build.VERSION_CODES.M
import android.os.Build.VERSION_CODES.S
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.kleine.R
import com.example.kleine.firebaseDatabase.FirebaseDb
import com.example.kleine.model.Product
import com.example.kleine.util.Constants.Companion.BLACK
import com.example.kleine.util.Constants.Companion.CHAIR_CATEGORY
import com.example.kleine.util.Constants.Companion.COLORS
import com.example.kleine.util.Constants.Companion.CUPBOARD_CATEGORY
import com.example.kleine.util.Constants.Companion.GREEN
import com.example.kleine.util.Constants.Companion.IMAGES
import com.example.kleine.util.Constants.Companion.ORANGE
import com.example.kleine.util.Constants.Companion.PRODUCTS_COLLECTION
import com.example.kleine.util.Constants.Companion.RED
import com.example.kleine.util.Constants.Companion.SIZES
import com.example.kleine.util.Constants.Companion.XLARGE
import com.example.kleine.viewmodel.lunchapp.KleineViewModel
import com.example.kleine.viewmodel.lunchapp.ViewModelProviderFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LunchActivity : AppCompatActivity() {
    lateinit var viewModel: KleineViewModel

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
        val firebaseDb = FirebaseDb()
        val viewModelFactory = ViewModelProviderFactory(firebaseDb)
        viewModel = ViewModelProvider(this,viewModelFactory)[KleineViewModel::class.java]

//        val random = Random.nextInt(from = 10000, until = 99999)

        saveNewProduct()
    }

    private fun saveNewProduct() {

        val title = "Cupboard 2"
        val description = "Original cupboard"
        val category = CUPBOARD_CATEGORY
        val price = "1200"
        val newPrice = null
        val seller = "mo mart"

        val images = HashMap<String,Any>()
        val imagesList = listOf(
            "https://firebasestorage.googleapis.com/v0/b/kleine-6e51a.appspot.com/o/products%2FUwlohue4OclECb0aABut%2Fcupbard1.jpg?alt=media&token=d491349f-a2fe-42bb-8d04-92dcdd9ca673",
            "https://firebasestorage.googleapis.com/v0/b/kleine-6e51a.appspot.com/o/products%2FUwlohue4OclECb0aABut%2Fcupbard1.jpg?alt=media&token=d491349f-a2fe-42bb-8d04-92dcdd9ca673",
            "https://firebasestorage.googleapis.com/v0/b/kleine-6e51a.appspot.com/o/products%2FUwlohue4OclECb0aABut%2Fcupbard1.jpg?alt=media&token=d491349f-a2fe-42bb-8d04-92dcdd9ca673"
        )

        images.put(IMAGES,imagesList.toList())

        val colors = HashMap<String,Any>()
        val colorsList = listOf<String>(
            GREEN,
            BLACK,
        )
        colors.put(COLORS, colorsList.toList())

        val sizes = HashMap<String,Any>()
        val sizesList = listOf(
            S,
            M,
            L,
            XLARGE
        )
        sizes.put(SIZES,sizesList.toList())

        val prodcut = Product(102862,title, description, category, price,newPrice, seller, images, colors, sizes)

        Firebase.firestore.collection(PRODUCTS_COLLECTION)
            .document("kT7n9tbCbOdJxzCx1Bwt")
            .set(prodcut)


    }
}