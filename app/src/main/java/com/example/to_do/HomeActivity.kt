package com.example.to_do

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.to_do.databinding.ActivityHomeBinding
import com.example.to_do.fragments.CompletedFragment
import com.example.to_do.fragments.HomeFragment
import com.example.to_do.fragments.UserFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class HomeActivity: AppCompatActivity() {
    // umozliwia userowi ingerowac, klikac, przewijac itd.

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            replaceFragment(HomeFragment(), R.id.fragments_container)
        }

        // z material design
        val navView: BottomNavigationView = binding.bottomNavView
        navView.setSelectedItemId(R.id.nav_home)


        // BOTTOM NAV LOOP
        navView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_completed -> {
                    settingNavigationHandler(CompletedFragment(), R.string.completed_title)
                    true
                }

                R.id.nav_home -> {
                    settingNavigationHandler(HomeFragment(), R.string.home_title)
                    true
                }

                R.id.nav_user -> {
                    settingNavigationHandler(UserFragment(), R.string.user_title)
                    true
                }

                else -> false
            }
        }
    }

    private fun settingNavigationHandler(fragment: Fragment, title: Int) {
        replaceFragment(fragment, R.id.fragments_container)
        setTitle(title)
    }

    private fun replaceFragment(fragment: Fragment, containerId: Int){
        // wyswietlanie, zarzadzanie
        supportFragmentManager.beginTransaction()
            .replace(containerId, fragment)
            .commit()
    }
}