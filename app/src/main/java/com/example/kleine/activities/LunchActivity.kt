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

        val title = "Table 3"
        val description = "Modern table 2022 Modern table 2022 Modern table 2022 Modern table 2022 Modern table 2022 Modern table 2022 " +
                "Modern table 2022 Modern table 2022 Modern table 2022 Modern table 2022 Modern table 2022 Modern table 2022" +
                "Modern table 2022 Modern table 2022 Modern table 2022 Modern table 2022 Modern table 2022 Modern table 2022"

        val category = TABLES_CATEGORY
        val price = "100"
        val newPrice = "20"
        val seller = "ps mart"
        val orders = 22

        val images = HashMap<String,Any>()
        val imagesList = listOf(
            "https://secure.img1-fg.wfcdn.com/im/01085742/resize-h755-w755%5Ecompr-r85/1022/102295873/Andresen+47.75%27%27+Console+Table.jpg",
            "https://secure.img1-fg.wfcdn.com/im/01085742/resize-h755-w755%5Ecompr-r85/1022/102295873/Andresen+47.75%27%27+Console+Table.jpg"
        )

        images.put(IMAGES,imagesList.toList())

        val colors = HashMap<String,Any>()
        val colorsList = listOf<String>(
            "#D8F0D6",
            "#B8D9FA",
        )
        colors.put(COLORS, colorsList.toList())

        val sizes = HashMap<String,Any>()
        val sizesList = listOf(
            Constants.SMALL,
            MEDIUM,
            LARGE,
            XLARGE
        )
        sizes.put(SIZES,sizesList.toList())

        val prodcut = Product(12311,title, description, category, newPrice,price, seller, images, colors, sizes,orders)

        Firebase.firestore.collection(PRODUCTS_COLLECTION)
            .document()
            .set(prodcut)
    }
}