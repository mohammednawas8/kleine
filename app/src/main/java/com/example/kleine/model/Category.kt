package com.example.kleine.model

data class Category(
    val name:String,
    val products:Int,
    val rank:Int
){
    constructor() : this("",0,0)
}
