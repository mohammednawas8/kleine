package com.example.kleine.model

data class Product(
    val title: String? = "",
    val description: String? = "",
    val category: String? = "",
    val oldPrice:String?="",
    val price: String? = "",
    val seller: String? = "",
    val images: HashMap<String, Any>?=null,
    val colors: HashMap<String, Any>?=null,
    val sizes: HashMap<String, Any>?=null
){
    constructor(
         title: String? = "",
         description: String? = "",
         category: String? = "",
         price: String? = "",
         seller: String? = "",
         images: HashMap<String, Any>,
         colors: HashMap<String, Any>,
         sizes: HashMap<String, Any>
    ) : this(title,description,category,null,price,seller, images, colors, sizes)

    constructor():this("","","","","",null,null,null)
}
