package com.example.kleine.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.kleine.R
import com.example.kleine.databinding.ActivityStoreBinding
import com.example.kleine.firebaseDatabase.FirebaseDb
import com.example.kleine.fragments.shopping.HomeFragment
import com.example.kleine.fragments.store.StoreHomeFragment
import com.example.kleine.fragments.store.StoreProductsFragment
import com.example.kleine.model.Store
import com.example.kleine.resource.Resource
import com.example.kleine.viewmodel.store.StoreViewModel
import com.example.kleine.viewmodel.store.StoreViewModelProviderFactory
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class StoreActivity : AppCompatActivity() {
    val viewModel: StoreViewModel by lazy {
        val database = FirebaseDb()
        val viewModelProvider = StoreViewModelProviderFactory(database)
        ViewModelProvider(this, viewModelProvider)[StoreViewModel::class.java]
    }
    private val TAG = "StoreActivity"
    private lateinit var binding: ActivityStoreBinding
    private lateinit var toggle: ActionBarDrawerToggle


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        setSupportActionBar(binding.topAppBar)
        toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            R.string.open_menu,
            R.string.close_menu
        )

        // Show the home fragment as the default fragment when the activity is opened
        replaceFragment(StoreHomeFragment())
        binding.navigationView.menu.getItem(0).isChecked = true

        itemMenuSelectedListener()

        observeStore()

    }

    private fun observeStore() {
        viewModel.store.observe(this) { response ->
            when (response) {
                is Resource.Loading -> {
                    showLoading()
                    Log.d(TAG, "LoadingStore")
                    return@observe
                }

                is Resource.Success -> {
                    val user = response.data
                    hideLoading()
                    fillUserInfoInNavigationDrawerHeader(user)
                    Log.d(TAG, "SuccessStore:${user?.name}")
                    return@observe
                }

                is Resource.Error -> {
                    Toast.makeText(this, R.string.error_occurred, Toast.LENGTH_SHORT).show()
                    hideLoading()
                    Log.e(TAG, response.message.toString())
                    return@observe
                }
            }
        }
    }

    private fun fillUserInfoInNavigationDrawerHeader(user: Store?) {
        val view = binding.navigationView.getHeaderView(0)
        val storeName = view.findViewById<TextView>(R.id.tv_store_name)
        storeName.text = user?.name
    }

    private fun hideLoading() {
    }

    private fun showLoading() {
    }


    private fun itemMenuSelectedListener() {
        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true

            when (menuItem.title) {
                "Home" ->{
                    replaceFragment(StoreHomeFragment())
                }
                "Products" -> {
                    replaceFragment(StoreProductsFragment())
                }

                "Logout" ->{
                    Firebase.auth.signOut()
                    val intent = Intent(this,LunchActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
            }

            binding.navigationView.menu.forEach { unselectedItems ->
                if (unselectedItems != menuItem)
                    unselectedItems.isChecked = false
            }
            binding.drawerLayout.close()
            true
        }
    }

    private fun replaceFragment(storeProductsFragment: Fragment) {
        val fragmentManger = supportFragmentManager
        val fragmentTransaction = fragmentManger.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_host, storeProductsFragment)
        fragmentTransaction.commit()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }

        when (item.itemId) {
            R.id.home -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

}