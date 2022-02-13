package com.example.kleine.viewmodel.lunchapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kleine.firebaseDatabase.FirebaseDb
import com.example.kleine.model.User

class KleineViewModel(
    private val firebaseDatabase: FirebaseDb
) : ViewModel() {

    val register = MutableLiveData<User>()
    val registerError = MutableLiveData<String>()

    val saveInformation = MutableLiveData<Boolean>()
    val saveInformationError = MutableLiveData<String>()

    val login = MutableLiveData<Boolean>()
    val loginError = MutableLiveData<String>()


    fun registerNewUser(
        user: User,
        password: String
    ) = firebaseDatabase.createNewUser(user.email, password).addOnCompleteListener {
        if (it.isSuccessful)
            register.postValue(user)
        else
            registerError.postValue(it.exception.toString())
    }

    fun saveUserInformation(
        userUid:String,
        user: User
    ) = firebaseDatabase.saveUserInformation(userUid,user).addOnCompleteListener {
        if(it.isSuccessful)
            saveInformation.postValue(true)
        else
            saveInformationError.postValue(it.exception.toString())

    }


    fun loginUser(
        email: String,
        password: String
    ) = firebaseDatabase.loginUser(email, password).addOnCompleteListener {
        if (it.isSuccessful)
            login.postValue(true)
        else
            loginError.postValue(it.exception.toString())
    }


}