package com.example.kleine.firebaseDatabase

import android.util.Log
import com.example.kleine.model.*
import com.example.kleine.util.Constants.Companion.ADDRESS_COLLECTION
import com.example.kleine.util.Constants.Companion.BEST_DEALS
import com.example.kleine.util.Constants.Companion.CART_COLLECTION
import com.example.kleine.util.Constants.Companion.CATEGORIES_COLLECTION
import com.example.kleine.util.Constants.Companion.CATEGORY
import com.example.kleine.util.Constants.Companion.CHAIR_CATEGORY
import com.example.kleine.util.Constants.Companion.CLOTHES
import com.example.kleine.util.Constants.Companion.COLOR
import com.example.kleine.util.Constants.Companion.CUPBOARD_CATEGORY
import com.example.kleine.util.Constants.Companion.ID
import com.example.kleine.util.Constants.Companion.ORDERS
import com.example.kleine.util.Constants.Companion.ORDER_CONFIRM_STATE
import com.example.kleine.util.Constants.Companion.ORDER_PLACED_STATE
import com.example.kleine.util.Constants.Companion.PRICE
import com.example.kleine.util.Constants.Companion.PRODUCTS_COLLECTION
import com.example.kleine.util.Constants.Companion.QUANTITY
import com.example.kleine.util.Constants.Companion.SIZE
import com.example.kleine.util.Constants.Companion.STORES_COLLECTION
import com.example.kleine.util.Constants.Companion.TITLE
import com.example.kleine.util.Constants.Companion.USERS_COLLECTION
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Transaction
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.random.Random

class FirebaseDb {
    private val usersCollectionRef = Firebase.firestore.collection(USERS_COLLECTION)
    private val productsCollection = Firebase.firestore.collection(PRODUCTS_COLLECTION)
    private val categoriesCollection = Firebase.firestore.collection(CATEGORIES_COLLECTION)
    private val storesCollection = Firebase.firestore.collection(STORES_COLLECTION)
    private val firebaseStorage = Firebase.storage.reference

    val userUid = FirebaseAuth.getInstance().currentUser?.uid

    private val userCartCollection = userUid?.let {
        Firebase.firestore.collection(USERS_COLLECTION).document(it).collection(CART_COLLECTION)
    }
    private val userAddressesCollection = userUid?.let {
        Firebase.firestore.collection(USERS_COLLECTION).document(it).collection(ADDRESS_COLLECTION)

    }


    private val firebaseAuth = Firebase.auth

    fun getProductsByCategory(category: String,page:Long) =
        productsCollection.whereEqualTo(CATEGORY,category).limit(page).get()


    fun getMostRequestedProducts(category: String,page:Long) =
        productsCollection.whereEqualTo(CATEGORY, category)
            .orderBy(ORDERS, Query.Direction.DESCENDING).limit(page).get()


    fun createNewUser(
        email: String, password: String
    ) = firebaseAuth.createUserWithEmailAndPassword(email, password)

    fun saveUserInformation(
        userUid: String,
        user: User
    ) = usersCollectionRef.document(userUid).set(user)

    fun loginUser(
        email: String,
        password: String
    ) = firebaseAuth.signInWithEmailAndPassword(email, password)

    fun getClothesProducts(pagingPage: Long) =
        productsCollection.whereEqualTo(CATEGORY, CLOTHES).limit(pagingPage).get()

    fun getBestDealsProducts(pagingPage: Long) =
        productsCollection.whereEqualTo(CATEGORY, BEST_DEALS).limit(pagingPage).get()

    fun getHomeProducts(pagingPage: Long) =
        productsCollection.limit(pagingPage).get()

    //add order by orders
    fun getMostOrderedCupboard(pagingPage: Long) =
        productsCollection.whereEqualTo(CATEGORY, CUPBOARD_CATEGORY).limit(pagingPage)
            .orderBy(ORDERS, Query.Direction.DESCENDING).limit(pagingPage).get()

    fun getCupboards(pagingPage: Long) =
        productsCollection.whereEqualTo(CATEGORY, CUPBOARD_CATEGORY).limit(pagingPage)
            .limit(pagingPage).get()

    fun addProductToCart(product: CartProduct) = userCartCollection?.document()!!.set(product)

    fun getProductInCart(product: CartProduct) = userCartCollection!!
        .whereEqualTo(ID, product.id)
        .whereEqualTo(COLOR, product.color)
        .whereEqualTo(SIZE, product.size).get()

    fun increaseProductQuantity(documentId: String): Task<Transaction> {
        val document = userCartCollection!!.document(documentId)
        return Firebase.firestore.runTransaction { transaction ->
            val productBefore = transaction.get(document)
            var quantity = productBefore.getLong(QUANTITY)
            quantity = quantity!! + 1
            transaction.update(document, QUANTITY, quantity)
        }

    }

    fun getItemsInCart() = userCartCollection!!

    fun decreaseProductQuantity(documentId: String): Task<Transaction> {
        val document = userCartCollection!!.document(documentId)
        return Firebase.firestore.runTransaction { transaction ->
            val productBefore = transaction.get(document)
            var quantity = productBefore.getLong(QUANTITY)
            quantity = if (quantity!!.toInt() == 1)
                1
            else
                quantity - 1
            transaction.update(document, QUANTITY, quantity)

        }

    }

    fun deleteProductFromCart(documentId: String) =
        userCartCollection!!.document(documentId).delete()


    fun searchProducts(searchQuery: String) = productsCollection
        .orderBy("title")
        .startAt(searchQuery)
        .endAt("\u03A9+$searchQuery")
        .limit(5)
        .get()

    fun getCategories() = categoriesCollection.orderBy("rank").get()

    fun getProductFromCartProduct(cartProduct: CartProduct) =
        productsCollection.whereEqualTo(ID, cartProduct.id)
            .whereEqualTo(TITLE, cartProduct.name)
            .whereEqualTo(PRICE, cartProduct.price).get()

    fun saveNewAddress(address: Address) = userAddressesCollection?.add(address)

    fun getAddresses() = userAddressesCollection

    fun findAddress(address: Address) = userAddressesCollection!!
        .whereEqualTo("addressTitle", address.addressTitle)
        .whereEqualTo("fullName", address.fullName).get()

    fun updateAddress(documentUid: String, address: Address) =
        userAddressesCollection?.document(documentUid)?.set(address)

    fun deleteAddress(documentUid: String, address: Address) =
        userAddressesCollection?.document(documentUid)?.delete()

    fun placeOrder(products: List<CartProduct>, address: Address, order: Order) =
        Firebase.firestore.runBatch { batch ->
            //filter every product to its store
            /**
             * create a map of products that has the size of stores list,
            the map has stores name as keys
             */

            val stores = ArrayList<String>()
            products.forEach { cartProduct ->
                if (!stores.contains(cartProduct.store)) {
                    stores.add(cartProduct.store)
                }
            }

            val productsMap = HashMap<String, ArrayList<CartProduct>>(stores.size)
            stores.forEach { storeName ->
                val tempList = ArrayList<CartProduct>()
                products.forEach { cartProduct ->
                    if (cartProduct.store == storeName)
                        tempList.add(cartProduct)
                    productsMap[storeName] = tempList
                }
            }


            /**
            // Adding order,address and products to each store
             */
            productsMap.forEach {
                val store = it.key
                val orderProducts = it.value
                val orderNum = order.id
                var price = 0

                orderProducts.forEach { it2 ->
                    if (it2.newPrice != null && it2.newPrice.isNotEmpty()) {
                        price += it2.newPrice.toInt() * it2.quantity
                    } else
                        price += it2.price.toInt() * it2.quantity
                }

                Log.d("test", "$store $price")

                val storeOrder = Order(
                    orderNum.toString(),
                    Calendar.getInstance().time,
                    price.toString(),
                    ORDER_PLACED_STATE
                )

                val storeDocument = storesCollection
                    .document(store)
                    .collection("orders")
                    .document()

                batch.set(storeDocument, storeOrder)

                val storeOrderAddress = storeDocument.collection(ADDRESS_COLLECTION).document()
                batch.set(storeOrderAddress, address)


                orderProducts.forEach {
                    val storeOrderProducts =
                        storeDocument.collection(PRODUCTS_COLLECTION).document()
                    batch.set(storeOrderProducts, it)
                }


            }

            /**
            // Adding order,address and products to the user
             */
            val userOrderDocument =
                usersCollectionRef.document(FirebaseAuth.getInstance().currentUser!!.uid)
                    .collection("orders").document()
            batch.set(userOrderDocument, order)

            products.forEach {
                val userProductDocument =
                    userOrderDocument.collection(PRODUCTS_COLLECTION).document()
                batch.set(userProductDocument, it)
            }

            val userAddressDocument = userOrderDocument.collection(ADDRESS_COLLECTION).document()

            batch.set(userAddressDocument, address)

        }.also {
            deleteCartItems()
        }

    private fun deleteCartItems() {
        userCartCollection?.get()?.addOnSuccessListener {
            Firebase.firestore.runBatch { batch ->
                it.documents.forEach {
                    val document = userCartCollection.document(it.id)
                    batch.delete(document)
                }
            }
        }
    }


    fun getUser() = usersCollectionRef
        .document(FirebaseAuth.getInstance().currentUser!!.uid)


    fun uploadUserProfileImage(image: ByteArray, imageName: String): UploadTask {
        val imageRef = firebaseStorage.child("profileImages")
            .child(firebaseAuth.currentUser!!.uid)
            .child(imageName)

        return imageRef.putBytes(image)

    }

    fun getImageUrl(
        firstName: String,
        lastName: String,
        email: String,
        imageName: String,
        onResult: (User?, String?) -> Unit,
    ) {
        if (imageName.isNotEmpty())
            firebaseStorage.child("profileImages")
                .child(firebaseAuth.currentUser!!.uid)
                .child(imageName).downloadUrl.addOnCompleteListener {
                    if (it.isSuccessful) {
                        val imageUrl = it.result.toString()
                        val user = User(firstName, lastName, email, imageUrl)
                        onResult(user, null)
                    } else
                        onResult(null, it.exception.toString())

                } else {
            val user = User(firstName, lastName, email, "")
            onResult(user, null)
        }
    }

    fun updateUserInformation(user: User) =
        Firebase.firestore.runTransaction { transaction ->
            val userPath = usersCollectionRef.document(Firebase.auth.currentUser!!.uid)
            if (user.imagePath.isNotEmpty()) {
                transaction.set(userPath, user)
            } else {
                val imagePath = transaction.get(userPath)["imagePath"] as String
                user.imagePath = imagePath
                transaction.set(userPath, user)
            }

        }


    fun getUserOrders() = usersCollectionRef
        .document(FirebaseAuth.getInstance().currentUser!!.uid)
        .collection(ORDERS)
        .orderBy("date", Query.Direction.DESCENDING)
        .get()

    fun resetPassword(email: String) = firebaseAuth.sendPasswordResetEmail(email)

    fun getOrderAddressAndProducts(
        order: Order,
        address: (Address?, String?) -> Unit,
        products: (List<CartProduct>?, String?) -> Unit
    ) {
        usersCollectionRef
            .document(Firebase.auth.currentUser!!.uid).collection(ORDERS)
            .whereEqualTo("id", order.id)
            .get().addOnCompleteListener {
                if (it.isSuccessful) {
                    val id = it.result?.documents?.get(0)?.id
                    usersCollectionRef.document(Firebase.auth.currentUser!!.uid)
                        .collection(ORDERS).document(id!!).collection(ADDRESS_COLLECTION).get()
                        .addOnCompleteListener { it2 ->
                            if (it2.isSuccessful) {
                                val address2 = it2.result?.toObjects(Address::class.java)
                                Log.d("test", address2!!.size.toString())
                                address(address2?.get(0), null)
                            } else
                                address(null, it2.exception.toString())
                        }

                    usersCollectionRef.document(Firebase.auth.currentUser!!.uid)
                        .collection(ORDERS).document(id).collection(PRODUCTS_COLLECTION).get()
                        .addOnCompleteListener { it2 ->
                            if (it2.isSuccessful) {
                                val products2 = it2.result?.toObjects(CartProduct::class.java)
                                Log.d("test", products2!!.size.toString())
                                products(products2, null)
                            } else
                                products(null, it2.exception.toString())
                        }


                } else {
                    address(null, it.exception.toString())
                    products(null, it.exception.toString())
                }
            }
    }

    //true -> already existed account
    //false -> new account
    fun checkUserByEmail(email: String, onResult: (String?, Boolean?) -> Unit) {
        usersCollectionRef.whereEqualTo("email", email).get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val user = it.result.toObjects(User::class.java)
                    if (user.isEmpty())
                        onResult(null, false)
                    else
                        onResult(null, true)
                } else
                    onResult(it.exception.toString(), null)
            }
    }

    fun signInWithGoogle(credential: AuthCredential) =
        FirebaseAuth.getInstance().signInWithCredential(credential)

    fun fetchStore(uid:String) = storesCollection.whereEqualTo("uid",uid).get()



    fun logout() = Firebase.auth.signOut()
}
