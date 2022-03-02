package com.example.kleine.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val firstName:String,
    val lastName:String,
    val email:String,
    val imagePath:String=""
):Parcelable{

    constructor() : this("","","")
}

