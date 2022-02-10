package com.example.kleine.model

data class User(
    val firstName:String,
    val lastName:String,
    val email:String,
    val imagePath:String=""
){

    constructor() : this("","","")
}

