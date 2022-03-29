package com.example.kleine.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import com.example.kleine.R
import com.example.kleine.databinding.ActivityStoreBinding
import com.example.kleine.fragments.BlankFragment
import com.example.kleine.fragments.categories.FurnitureFragment
import com.example.kleine.fragments.categories.TableFragment

class StoreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoreBinding
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        setSupportActionBar(binding.topAppBar)
        toggle = ActionBarDrawerToggle(this,binding.drawerLayout,R.string.open_menu,R.string.close_menu)

        val fragmentManger = supportFragmentManager
        val fragmentTransaction = fragmentManger.beginTransaction()
//        fragmentTransaction.replace(R.id.fragment_host,BlankFragment())
        fragmentTransaction.commit()

        binding.navigationView.menu.getItem(0).isChecked = true

        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true

            binding.navigationView.menu.forEach { unselectedItems ->
                if(unselectedItems != menuItem)
                    unselectedItems.isChecked = false
            }
            binding.drawerLayout.close()
            true
        }



    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }

        when(item.itemId){
            R.id.home -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

}