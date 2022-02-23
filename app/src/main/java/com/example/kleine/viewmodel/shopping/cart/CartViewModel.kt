package com.example.kleine.viewmodel.shopping.cart

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kleine.firebaseDatabase.FirebaseDb
import com.example.kleine.model.CartProduct
import com.example.kleine.model.Product
import com.example.kleine.resource.Resource

class CartViewModel(
) : ViewModel() {
    val cartProducts = MutableLiveData<Resource<List<CartProduct>>>()
    val cartItemsCount = MutableLiveData<Resource<Int>>()
    val plus = MutableLiveData<Resource<Int>>()
    val minus = MutableLiveData<Resource<Int>>()
    val deleteProduct = MutableLiveData<Resource<Boolean>>()
    val product = MutableLiveData<Resource<Product>>()
    var firebaseDb: FirebaseDb = FirebaseDb()


    init {
        getItemsInCart()
    }

    private fun getItemsInCart() {
        cartProducts.postValue(Resource.Loading())
        cartItemsCount.postValue(Resource.Loading())

        firebaseDb.getItemsInCart().addSnapshotListener { value, error ->
            if (error != null) {
                cartProducts.postValue(Resource.Error(error.message))
                cartItemsCount.postValue(Resource.Error(error.message))
            } else {
                val products = value!!.toObjects(CartProduct::class.java)
                cartProducts.postValue(Resource.Success(products))
                cartItemsCount.postValue(Resource.Success(products.size))
            }
        }
    }

    fun increaseQuantity(product: CartProduct) {
        plus.postValue(Resource.Loading())
        firebaseDb.getProductInCart(product).addOnCompleteListener {
            if (it.isSuccessful) {
                val productToIncrease = it.result!!.documents[0]
                firebaseDb.increaseProductQuantity(productToIncrease.id)
                    .addOnCompleteListener { increase ->
                        if (increase.isSuccessful)
                            plus.postValue(Resource.Success(product.quantity + 1))
                        else
                            plus.postValue(Resource.Error(increase.exception.toString()))
                    }
            } else
                plus.postValue(Resource.Error(it.exception.toString()))

        }
    }

    fun decreaseQuantity(product: CartProduct) {
        minus.postValue(Resource.Loading())
        firebaseDb.getProductInCart(product).addOnCompleteListener {
            if (it.isSuccessful) {
                val productToIncrease = it.result!!.documents[0]
                firebaseDb.decreaseProductQuantity(productToIncrease.id)
                    .addOnCompleteListener { decrease ->
                        if (decrease.isSuccessful)
                            minus.postValue(Resource.Success(product.quantity + 1))
                        else
                            minus.postValue(Resource.Error(decrease.exception.toString()))
                    }
            } else
                minus.postValue(Resource.Error(it.exception.toString()))

        }
    }

    fun deleteProductFromCart(product: CartProduct) {
        deleteProduct.postValue(Resource.Loading())
        firebaseDb.getProductInCart(product).addOnCompleteListener { productToDelete ->
            if (productToDelete.isSuccessful) {
                val documentId = productToDelete.result!!.documents[0].id
                firebaseDb.deleteProductFromCart(documentId).addOnCompleteListener {
                    if (it.isSuccessful)
                        deleteProduct.postValue(Resource.Success(true))
                    else
                        deleteProduct.postValue(Resource.Error(it.exception.toString()))
                }

            } else
                deleteProduct.postValue(Resource.Error(productToDelete.exception.toString()))
        }
    }

    fun getProductFromCartProduct(cartProduct: CartProduct) {
        product.postValue(Resource.Loading())
        firebaseDb.getProductFromCartProduct(cartProduct).addOnCompleteListener {
            if (it.isSuccessful) {
                val tempProduct = it.result!!.toObjects(Product::class.java)[0]
                product.postValue(Resource.Success(tempProduct))
            } else
                product.postValue(Resource.Error(it.exception.toString()))
        }
    }
}