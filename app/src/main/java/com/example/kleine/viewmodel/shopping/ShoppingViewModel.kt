package com.example.kleine.viewmodel.shopping

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kleine.firebaseDatabase.FirebaseDb
import com.example.kleine.model.*
import com.example.kleine.resource.Resource
import com.example.kleine.util.Constants.Companion.ACCESSORY_CATEGORY
import com.example.kleine.util.Constants.Companion.CHAIR_CATEGORY
import com.example.kleine.util.Constants.Companion.CUPBOARD_CATEGORY
import com.example.kleine.util.Constants.Companion.FURNITURE_CATEGORY
import com.example.kleine.util.Constants.Companion.TABLES_CATEGORY
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

private const val TAG = "ShoppingViewModel"

class ShoppingViewModel(
    private val firebaseDatabase: FirebaseDb
) : ViewModel() {

    val clothes = MutableLiveData<List<Product>>()
    val emptyClothes = MutableLiveData<Boolean>()
    val bestDeals = MutableLiveData<List<Product>>()
    val emptyBestDeals = MutableLiveData<Boolean>()

    val home = MutableLiveData<Resource<List<Product>>>()

    val chairs = MutableLiveData<Resource<List<Product>>>()
    val mostRequestedChairs = MutableLiveData<Resource<List<Product>>>()

    val tables = MutableLiveData<Resource<List<Product>>>()
    val mostRequestedTables = MutableLiveData<Resource<List<Product>>>()

    val accessory = MutableLiveData<Resource<List<Product>>>()
    val mostRequestedAccessories = MutableLiveData<Resource<List<Product>>>()

    val furniture = MutableLiveData<Resource<List<Product>>>()
    val mostRequestedFurniture = MutableLiveData<Resource<List<Product>>>()

    val mostRequestedCupboard = MutableLiveData<Resource<List<Product>>>()
    val cupboard = MutableLiveData<Resource<List<Product>>>()
    val addToCart = MutableLiveData<Resource<Boolean>>()

    val addAddress = MutableLiveData<Resource<Address>>()
    val updateAddress = MutableLiveData<Resource<Address>>()
    val deleteAddress = MutableLiveData<Resource<Address>>()

    val profile = MutableLiveData<Resource<User>>()

    val uploadProfileImage = MutableLiveData<Resource<String>>()
    val updateUserInformation = MutableLiveData<Resource<User>>()

    val userOrders = MutableLiveData<Resource<List<Order>>>()

    val passwordReset = MutableLiveData<Resource<String>>()

    val orderAddress = MutableLiveData<Resource<Address>>()
    val orderProducts = MutableLiveData<Resource<List<CartProduct>>>()

    val categories = MutableLiveData<Resource<List<Category>>>()


    val search = MutableLiveData<Resource<List<Product>>>()

    private var homePage: Long = 10
    private var clothesPaging: Long = 5
    private var bestDealsPaging: Long = 5

    private var cupboardPaging: Long = 4
    private var mostOrderCupboardPaging: Long = 5

    private var mostRequestedChairsPage: Long = 3
    private var chairsPage: Long = 4

    private var mostRequestedTablePage: Long = 3
    private var tablePage: Long = 4

    private var mostRequestedAccessoryPage: Long = 3
    private var accessoryPage: Long = 4

    private var mostRequestedFurniturePage: Long = 3
    private var furniturePage: Long = 4


    init {
        getClothesProducts()
        getBestDealsProduct()
        getHomeProduct()
    }

    private var furnitureProducts: List<Product>? = null
    fun getFurniture(size: Int = 0) {
        if (furnitureProducts != null && size == 0) {
            furniture.postValue(Resource.Success(furnitureProducts))
            return
        }
        furniture.postValue(Resource.Loading())
        shouldPaging(FURNITURE_CATEGORY, size) { shouldPaging ->
            if (shouldPaging) {
                tables.postValue(Resource.Loading())
                firebaseDatabase.getProductsByCategory(FURNITURE_CATEGORY, furniturePage)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val documents = it.result
                            if (!documents!!.isEmpty) {
                                val productsList = documents.toObjects(Product::class.java)
                                furnitureProducts = productsList
                                furniture.postValue(Resource.Success(productsList))
                                furniturePage += 4

                            }
                        } else
                            furniture.postValue(Resource.Error(it.exception.toString()))
                    }
            } else
                furniture.postValue(Resource.Error("Cannot paging"))

        }
    }

    private var mostRequestedFurnitureProducts: List<Product>? = null
    fun getMostRequestedFurniture(size: Int = 0) {
        if (mostRequestedFurnitureProducts != null && size == 0) {
            mostRequestedFurniture.postValue(Resource.Success(mostRequestedFurnitureProducts))
            return
        }
        mostRequestedFurniture.postValue(Resource.Loading())
        shouldPaging(FURNITURE_CATEGORY, size) { shouldPaging ->
            if (shouldPaging) {
                mostRequestedFurniture.postValue(Resource.Loading())
                firebaseDatabase.getProductsByCategory(
                    FURNITURE_CATEGORY,
                    mostRequestedFurniturePage
                )
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val documents = it.result
                            if (!documents!!.isEmpty) {
                                val productsList = documents.toObjects(Product::class.java)
                                mostRequestedFurnitureProducts = productsList
                                mostRequestedFurniture.postValue(Resource.Success(productsList))
                                mostRequestedFurniturePage += 4

                            }
                        } else
                            mostRequestedFurniture.postValue(Resource.Error(it.exception.toString()))
                    }
            } else
                mostRequestedFurniture.postValue(Resource.Error("Cannot paging"))
        }
    }

    private var accessoriesProducts: List<Product>? = null
    fun getAccessories(size: Int = 0) {
        if (accessoriesProducts != null && size == 0) {
            accessory.postValue(Resource.Success(accessoriesProducts))
            return
        }
        accessory.postValue(Resource.Loading())
        shouldPaging(ACCESSORY_CATEGORY, size) { shouldPaging ->
            if (shouldPaging) {
                Log.d("test", "paging")
                firebaseDatabase.getProductsByCategory(ACCESSORY_CATEGORY, accessoryPage)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val documents = it.result
                            if (!documents!!.isEmpty) {
                                val productsList = documents.toObjects(Product::class.java)
                                accessory.postValue(Resource.Success(productsList))
                                accessoriesProducts = productsList
                                accessoryPage += 4

                            }
                        } else
                            accessory.postValue(Resource.Error(it.exception.toString()))
                    }
            } else {
                accessory.postValue(Resource.Error("Cannot page"))
            }
        }
    }

    private var mostRequestedAccessoriesProducts: List<Product>? = null
    fun getMostRequestedAccessories(size: Int = 0) {
        if (mostRequestedAccessoriesProducts != null && size == 0) {
            mostRequestedAccessories.postValue(Resource.Success(mostRequestedAccessoriesProducts))
            return
        }
        mostRequestedAccessories.postValue(Resource.Loading())
        shouldPaging(ACCESSORY_CATEGORY, size) { shouldPaging ->
            if (shouldPaging) {
                chairs.postValue(Resource.Loading())
                firebaseDatabase.getProductsByCategory(
                    ACCESSORY_CATEGORY,
                    mostRequestedAccessoryPage
                )
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val documents = it.result
                            if (!documents!!.isEmpty) {
                                val productsList = documents.toObjects(Product::class.java)
                                mostRequestedAccessories.postValue(Resource.Success(productsList))
                                mostRequestedAccessoriesProducts = productsList
                                mostRequestedAccessoryPage += 4

                            }
                        } else
                            mostRequestedAccessories.postValue(Resource.Error(it.exception.toString()))
                    }
            } else
                mostRequestedAccessories.postValue(Resource.Error("Cannot paging"))
        }
    }

    private var chairsProducts: List<Product>? = null
    fun getChairs(size: Int = 0) {
        if (chairsProducts != null && size == 0) {
            chairs.postValue(Resource.Success(chairsProducts))
            return
        }
        chairs.postValue(Resource.Loading())
        shouldPaging(CUPBOARD_CATEGORY, size) { shouldPaging ->
            if (shouldPaging) {

                chairs.postValue(Resource.Loading())
                firebaseDatabase.getProductsByCategory(CHAIR_CATEGORY, chairsPage)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val documents = it.result
                            if (!documents!!.isEmpty) {
                                val productsList = documents.toObjects(Product::class.java)
                                chairsProducts = productsList
                                chairs.postValue(Resource.Success(productsList))
                                chairsPage += 4

                            }
                        } else
                            chairs.postValue(Resource.Error(it.exception.toString()))
                    }
            } else
                chairs.postValue(Resource.Error("Cannot paging"))
        }
    }

    private var mostRequestedChairsProducts: List<Product>? = null
    fun getMostRequestedChairs(size: Int = 0) {
        if (mostRequestedChairsProducts != null && size == 0) {
            mostRequestedChairs.postValue(Resource.Success(chairsProducts))
            return
        }
        mostRequestedChairs.postValue(Resource.Loading())
        shouldPaging(CUPBOARD_CATEGORY, size) { shouldPaging ->
            if (shouldPaging) {
                chairs.postValue(Resource.Loading())
                firebaseDatabase.getProductsByCategory(CHAIR_CATEGORY, mostRequestedChairsPage)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val documents = it.result
                            if (!documents!!.isEmpty) {
                                val productsList = documents.toObjects(Product::class.java)
                                mostRequestedChairsProducts = productsList
                                mostRequestedChairs.postValue(Resource.Success(productsList))
                                mostRequestedChairsPage += 4

                            }
                        } else
                            mostRequestedChairs.postValue(Resource.Error(it.exception.toString()))
                    }
            } else
                chairs.postValue(Resource.Error("Cannot paging"))
        }
    }

    private var tablesProducts: List<Product>? = null
    fun getTables(size: Int = 0) {
        if (tablesProducts != null && size == 0) {
            tables.postValue(Resource.Success(tablesProducts))
            return
        }
        tables.postValue(Resource.Loading())
        shouldPaging(TABLES_CATEGORY, size) { shouldPaging ->
            if (shouldPaging) {
                tables.postValue(Resource.Loading())
                firebaseDatabase.getProductsByCategory(TABLES_CATEGORY, tablePage)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val documents = it.result
                            if (!documents!!.isEmpty) {
                                val productsList = documents.toObjects(Product::class.java)
                                tablesProducts = productsList
                                tables.postValue(Resource.Success(productsList))
                                tablePage += 4

                            }
                        } else
                            tables.postValue(Resource.Error(it.exception.toString()))
                    }
            } else
                home.postValue(Resource.Error("Cannot paging"))
        }
    }

    private var mostRequestedTablesProducts: List<Product>? = null
    fun getMostRequestedTables(size: Int = 0) {
        if (mostRequestedTablesProducts != null && size == 0) {
            tables.postValue(Resource.Success(mostRequestedTablesProducts))
            return
        }
        mostRequestedTables.postValue(Resource.Loading())
        shouldPaging(TABLES_CATEGORY, size) { shouldPaging ->
            if (shouldPaging) {
                mostRequestedTables.postValue(Resource.Loading())
                firebaseDatabase.getProductsByCategory(TABLES_CATEGORY, mostRequestedTablePage)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val documents = it.result
                            if (!documents!!.isEmpty) {
                                val productsList = documents.toObjects(Product::class.java)
                                mostRequestedTablesProducts = productsList
                                mostRequestedTables.postValue(Resource.Success(productsList))
                                mostRequestedTablePage += 3

                            }
                        } else
                            mostRequestedTables.postValue(Resource.Error(it.exception.toString()))
                    }
            } else
                mostRequestedTables.postValue(Resource.Error("Cannot paging"))
        }
    }


    fun getClothesProducts() =
        firebaseDatabase.getClothesProducts(clothesPaging).addOnCompleteListener {
            if (it.isSuccessful) {
                val documents = it.result
                if (!documents!!.isEmpty) {
                    val productsList = documents.toObjects(Product::class.java)
                    clothes.postValue(productsList)
                    clothesPaging += 5
                } else
                    emptyClothes.postValue(true)

            } else
                Log.e(TAG, it.exception.toString())

        }

    fun getBestDealsProduct() =
        firebaseDatabase.getBestDealsProducts(bestDealsPaging).addOnCompleteListener {
            if (it.isSuccessful) {
                val documents = it.result
                if (!documents!!.isEmpty) {
                    val productsList = documents.toObjects(Product::class.java)
                    bestDeals.postValue(productsList)
                    bestDealsPaging += 5
                } else
                    emptyBestDeals.postValue(true)

            } else
                Log.e(TAG, it.exception.toString())
        }

    fun getHomeProduct(size: Int = 0) {
        home.postValue(Resource.Loading())
        shouldPagingHome(size)
        { shouldPaging ->
            if (shouldPaging) {
                home.postValue(Resource.Loading())
                firebaseDatabase.getHomeProducts(homePage)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val documents = it.result
                            if (!documents!!.isEmpty) {
                                val productsList = documents.toObjects(Product::class.java)
                                home.postValue(Resource.Success(productsList))
                                homePage += 4

                            }
                        } else
                            home.postValue(Resource.Error(it.exception.toString()))
                    }
            } else
                home.postValue(Resource.Error("Cannot paging"))
        }
    }

    private var mostRequestedCupboardProducts: List<Product>? = null
    fun getMostRequestedCupboards(size: Int = 0) {
        if (mostRequestedCupboardProducts != null && size == 0) {
            mostRequestedCupboard.postValue(Resource.Success(mostRequestedCupboardProducts))
            return
        }

        mostRequestedCupboard.postValue(Resource.Loading())
        shouldPaging(CUPBOARD_CATEGORY, size) { shouldPaging ->
            if (shouldPaging) {
                mostRequestedCupboard.postValue(Resource.Loading())
                firebaseDatabase.getMostOrderedCupboard(mostOrderCupboardPaging)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val documents = it.result
                            if (!documents!!.isEmpty) {
                                val productsList = documents.toObjects(Product::class.java)
                                mostRequestedCupboardProducts = productsList
                                mostRequestedCupboard.postValue(Resource.Success(productsList))
                                mostOrderCupboardPaging += 5

                            }
                        } else
                            mostRequestedCupboard.postValue(Resource.Error(it.exception.toString()))
                    }


            } else
                mostRequestedCupboard.postValue(Resource.Error("Cannot paging"))
        }
    }

    private var dCupboardProducts: List<Product>? = null
    fun getCupboardProduct(size: Int = 0) {
        if (dCupboardProducts != null && size == 0) {
            cupboard.postValue(Resource.Success(dCupboardProducts))
            return
        }
        shouldPaging(CUPBOARD_CATEGORY, size) { shouldPaging ->
            if (shouldPaging) {
                cupboard.postValue(Resource.Loading())
                firebaseDatabase.getCupboards(cupboardPaging).addOnCompleteListener {
                    if (it.isSuccessful) {

                        val documents = it.result
                        if (!documents!!.isEmpty) {
                            val productsList = documents.toObjects(Product::class.java)
                            dCupboardProducts = productsList
                            cupboard.postValue(Resource.Success(productsList))
                            cupboardPaging += 10
                        }

                    } else
                        cupboard.postValue(Resource.Error(it.exception.toString()))
                }
            } else
                cupboard.postValue(Resource.Error("Cannot paging"))
        }
    }

    /*
    * TODO : Move these functions to firebaseDatabase class
     */

    private fun shouldPaging(category: String, listSize: Int, onSuccess: (Boolean) -> Unit) {
        FirebaseFirestore.getInstance()
            .collection("categories")
            .whereEqualTo("name", category).get().addOnSuccessListener {
                val tempCategory = it.toObjects(Category::class.java)
                val products = tempCategory[0].products
                Log.d("test", " $category : prodcuts ${tempCategory[0].products}, size $listSize")
                if (listSize == products)
                    onSuccess(false).also { Log.d(TAG, "$category Paging:false") }
                else
                    onSuccess(true).also { Log.d(TAG, "$category Paging:true") }
            }
    }

    private fun shouldPagingHome(listSize: Int, onSuccess: (Boolean) -> Unit) {
        FirebaseFirestore.getInstance()
            .collection("categories").get().addOnSuccessListener {
                var productsCount = 0
                it.toObjects(Category::class.java).forEach { category ->
                    productsCount += category.products!!.toInt()
                }

                if (listSize == productsCount)
                    onSuccess(false)
                else
                    onSuccess(true)

            }
    }


    private fun checkIfProductAlreadyAdded(
        product: CartProduct,
        onSuccess: (Boolean, String) -> Unit
    ) {
        addToCart.postValue(Resource.Loading())
        firebaseDatabase.getProductInCart(product).addOnCompleteListener {
            if (it.isSuccessful) {
                val documents = it.result!!.documents
                if (documents.isNotEmpty())
                    onSuccess(true, documents[0].id) // true ---> product is already in cart
                else
                    onSuccess(false, "") // false ---> product is not in cart
            } else
                addToCart.postValue(Resource.Error(it.exception.toString()))

        }
    }


    fun addProductToCart(product: CartProduct) =
        checkIfProductAlreadyAdded(product) { isAdded, id ->
            if (isAdded) {
                firebaseDatabase.increaseProductQuantity(id).addOnCompleteListener {
                    if (it.isSuccessful)
                        addToCart.postValue(Resource.Success(true))
                    else
                        addToCart.postValue(Resource.Error(it.exception!!.message))

                }
            } else {
                firebaseDatabase.addProductToCart(product).addOnCompleteListener {
                    if (it.isSuccessful)
                        addToCart.postValue(Resource.Success(true))
                    else
                        addToCart.postValue(Resource.Error(it.exception!!.message))
                }
            }
        }


    fun saveAddress(address: Address) {
        addAddress.postValue(Resource.Loading())
        firebaseDatabase.saveNewAddress(address)?.addOnCompleteListener {
            if (it.isSuccessful)
                addAddress.postValue(Resource.Success(address))
            else
                addAddress.postValue(Resource.Error(it.exception.toString()))
        }
    }

    fun updateAddress(oldAddress: Address, newAddress: Address) {
        updateAddress.postValue(Resource.Loading())
        firebaseDatabase.findAddress(oldAddress).addOnCompleteListener { addressResponse ->
            if (addressResponse.isSuccessful) {
                val documentUid = addressResponse.result!!.documents[0].id
                firebaseDatabase.updateAddress(documentUid, newAddress)?.addOnCompleteListener {
                    if (it.isSuccessful)
                        updateAddress.postValue(Resource.Success(newAddress))
                    else
                        updateAddress.postValue(Resource.Error(it.exception.toString()))

                }

            } else
                updateAddress.postValue(Resource.Error(addressResponse.exception.toString()))

        }
    }

    fun deleteAddress(address: Address) {
        deleteAddress.postValue(Resource.Loading())
        firebaseDatabase.findAddress(address).addOnCompleteListener { addressResponse ->
            if (addressResponse.isSuccessful) {
                val documentUid = addressResponse.result!!.documents[0].id
                firebaseDatabase.deleteAddress(documentUid, address)?.addOnCompleteListener {
                    if (it.isSuccessful)
                        deleteAddress.postValue(Resource.Success(address))
                    else
                        deleteAddress.postValue(Resource.Error(it.exception.toString()))

                }

            } else
                deleteAddress.postValue(Resource.Error(addressResponse.exception.toString()))

        }
    }

    private val user: User? = null
    fun getUser() {
        if (user != null) {
            profile.postValue(Resource.Success(user))
            return
        }

        profile.postValue(Resource.Loading())
        firebaseDatabase.getUser().addSnapshotListener { value, error ->
            if (error != null)
                profile.postValue(Resource.Error(error.message))
            else
                profile.postValue(Resource.Success(value?.toObject(User::class.java)))

        }
    }

    fun uploadProfileImage(image: ByteArray) {
        uploadProfileImage.postValue(Resource.Loading())
        val name = UUID.nameUUIDFromBytes(image).toString()
        firebaseDatabase.uploadUserProfileImage(image, name).addOnCompleteListener {
            if (it.isSuccessful)
                uploadProfileImage.postValue(Resource.Success(name))
            else
                uploadProfileImage.postValue(Resource.Error(it.exception.toString()))
        }
    }

    fun updateInformation(firstName: String, lastName: String, email: String, imageName: String) {
        updateUserInformation.postValue(Resource.Loading())

        firebaseDatabase.getImageUrl(firstName, lastName, email, imageName) { user, exception ->

            if (exception != null)
                updateUserInformation.postValue(Resource.Error(exception))
                    .also { Log.d("test1", "up") }
            else
                user?.let {
                    onUpdateInformation(user).also { Log.d("test1", "down") }
                }
        }
    }

    private fun onUpdateInformation(user: User) {
        firebaseDatabase.updateUserInformation(user).addOnCompleteListener {
            if (it.isSuccessful)
                updateUserInformation.postValue(Resource.Success(user))
            else
                updateUserInformation.postValue(Resource.Error(it.exception.toString()))

        }
    }

    fun getUserOrders() {
        userOrders.postValue(Resource.Loading())
        firebaseDatabase.getUserOrders().addOnCompleteListener {
            if (it.isSuccessful)
                userOrders.postValue(Resource.Success(it.result?.toObjects(Order::class.java)))
            else
                userOrders.postValue(Resource.Error(it.exception.toString()))
        }
    }

    fun resetPassword(email: String) {
        passwordReset.postValue(Resource.Loading())
        firebaseDatabase.resetPassword(email).addOnCompleteListener {
            if (it.isSuccessful)
                passwordReset.postValue(Resource.Success(email))
            else
                passwordReset.postValue(Resource.Error(it.exception.toString()))
        }
    }

    fun getOrderAddressAndProducts(order: Order) {
        orderAddress.postValue(Resource.Loading())
        orderProducts.postValue(Resource.Loading())
        firebaseDatabase.getOrderAddressAndProducts(order, { address, aError ->
            if (aError != null)
                orderAddress.postValue(Resource.Error(aError))
            else
                orderAddress.postValue(Resource.Success(address))
        }, { products, pError ->

            if (pError != null)
                orderProducts.postValue(Resource.Error(pError))
            else
                orderProducts.postValue(Resource.Success(products))

        })
    }

    fun searchProducts(searchQuery: String) {
        search.postValue(Resource.Loading())
        firebaseDatabase.searchProducts(searchQuery).addOnCompleteListener {
            if (it.isSuccessful) {
                val productsList = it.result!!.toObjects(Product::class.java)
                search.postValue(Resource.Success(productsList))

            } else
                search.postValue(Resource.Error(it.exception.toString()))

        }
    }

    private var categoriesSafe: List<Category>? = null
    fun getCategories() {
        if(categoriesSafe != null){
            categories.postValue(Resource.Success(categoriesSafe))
            return
        }
        categories.postValue(Resource.Loading())
        firebaseDatabase.getCategories().addOnCompleteListener {
            if (it.isSuccessful) {
                val categoriesList = it.result!!.toObjects(Category::class.java)
                categoriesSafe = categoriesList
                categories.postValue(Resource.Success(categoriesList))
            } else
                categories.postValue(Resource.Error(it.exception.toString()))
        }


    }

}