package com.example.kleine.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kleine.firebaseDatabase.FirebaseDb
import com.example.kleine.model.User

class KleineViewModel(
    private val firebaseDatabase: FirebaseDb
) : ViewModel() {

     val register = MutableLiveData<User>()
     val registerError = MutableLiveData<String>()

    fun registerNewUser(
        user: User,
        password: String
    ) = firebaseDatabase.createNewUser(user.email, password).addOnCompleteListener {
        if (it.isSuccessful)
            register.postValue(user)
        else
            registerError.postValue(it.exception.toString())
    }




}