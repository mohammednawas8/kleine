package com.example.kleine.firebaseDatabase

import com.example.kleine.model.User
import com.example.kleine.util.Constants.Companion.BEST_DEALS
import com.example.kleine.util.Constants.Companion.CATEGORY
import com.example.kleine.util.Constants.Companion.CHAIR_CATEGORY
import com.example.kleine.util.Constants.Companion.CLOTHES
import com.example.kleine.util.Constants.Companion.CUPBOARD_CATEGORY
import com.example.kleine.util.Constants.Companion.ORDERS
import com.example.kleine.util.Constants.Companion.PRODUCTS_COLLECTION
import com.example.kleine.util.Constants.Companion.USERS_COLLECTION
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirebaseDb {
    private val usersCollectionRef = Firebase.firestore.collection(USERS_COLLECTION)
    private val productsCollection = Firebase.firestore.collection(PRODUCTS_COLLECTION)
    private val firebaseAuth = Firebase.auth

    fun createNewUser(
        email: String, password: String
    ) = firebaseAuth.createUserWithEmailAndPassword(email, password)

    fun saveUserInformation(
        userUid:String,
        user: User
    ) = usersCollectionRef.document(userUid).set(user)

    fun loginUser(
        email: String,
        password: String
    ) = firebaseAuth.signInWithEmailAndPassword(email, password)

    fun getClothesProducts(pagingPage:Long) = productsCollection.whereEqualTo(CATEGORY, CLOTHES).limit(pagingPage).get()

    fun getBestDealsProducts(pagingPage:Long) = productsCollection.whereEqualTo(CATEGORY, BEST_DEALS).limit(pagingPage).get()

    fun getChairs(pagingPage:Long) = productsCollection.whereEqualTo(CATEGORY, CHAIR_CATEGORY).limit(pagingPage).get()

    //add order by orders
    fun getMostOrderedCupboard(pagingPage:Long,category:String) = productsCollection.whereEqualTo(CATEGORY,
        category).limit(pagingPage).orderBy(ORDERS,Query.Direction.DESCENDING).limit(pagingPage).get()

    fun getCupboards(pagingPage:Long) = productsCollection.whereEqualTo(CATEGORY,CUPBOARD_CATEGORY).limit(pagingPage).limit(pagingPage).get()

}