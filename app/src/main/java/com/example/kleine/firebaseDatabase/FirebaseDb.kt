package com.example.kleine.firebaseDatabase

import com.example.kleine.util.Constants.Companion.USERS_COLLECTION
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirebaseDb {
    private val usersCollectionRef = Firebase.firestore.collection(USERS_COLLECTION)
    private val firebaseAuth = Firebase.auth

    fun createNewUser(email: String, password: String) = firebaseAuth.createUserWithEmailAndPassword(email,password)




}