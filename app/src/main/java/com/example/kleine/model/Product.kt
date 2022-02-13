package com.example.kleine.model

data class Product(
    val id :Int,
    val title: String? = "",
    val description: String? = "",
    val category: String? = "",
    val newPrice:String?="",
    val price: String? = "",
    val seller: String? = "",
    val images: HashMap<String, Any>?=null,
    val colors: HashMap<String, Any>?=null,
    val sizes: HashMap<String, Any>?=null
){
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
    ) : this(id,title,description,category,null,price,seller, images, colors, sizes)

    constructor():this(0,"","","","",null,null,null)
}
