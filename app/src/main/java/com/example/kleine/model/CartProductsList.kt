package com.example.kleine.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class CartProductsList(
    val products: @RawValue List<CartProduct>
) : Parcelable {


}
