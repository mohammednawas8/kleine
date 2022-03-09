package com.example.kleine.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import java.util.*
import kotlin.collections.HashMap


@Parcelize
data class Product(
    val id :Int,
    val title: String? = "",
    val description: String? = "",
    val category: String? = "",
    val newPrice:String?="",
    val price: String? = "",
    val seller: String? = "",

    val images:@RawValue HashMap<String, Any>?=null,
    val colors:@RawValue HashMap<String, Any>?=null,
    val sizes:@RawValue HashMap<String, Any>?=null,
    val orders:Int = 0,
    val offerTime:Date? = null,
    val sizeUnit:String?=null

) : Parcelable
    {
    constructor(
         id :Int,
         title: String? = "",
         description: String? = "",
         category: String? = "",
         price: String? = "",
         seller: String? = "",
         images: HashMap<String, Any>,
         colors: HashMap<String, Any>,
         sizes: HashMap<String, Any>
    ) : this(id,title,description,category,null,price,seller, images, colors, sizes,0,null,null)

    constructor():this(0,"","","","",null,null,null)
}
