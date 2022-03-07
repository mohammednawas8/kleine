package com.example.kleine.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class CartProduct (
    val id:Int,
    val name:String,
    val store:String,
    val image:String,
    val price:String,
    val newPrice:String?,
    val quantity:Int,
    val color:String,
    val size:String
        ) {
    constructor() : this(0,"","","","","",0,"","")
}
