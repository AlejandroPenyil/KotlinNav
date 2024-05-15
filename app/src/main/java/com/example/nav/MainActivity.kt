package com.example.nav

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.nav.databinding.ActivityMainBinding
import com.example.nav.ui.FirstFragment
import com.example.nav.ui.FourthFragment
import com.example.nav.ui.SecondFragment
import com.example.nav.ui.ThirdFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    //private lateinit var bottomNavigationView: BottomNavigationView

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //VIEJO
//        val firstFragment= FirstFragment()
//        val secondFragment= SecondFragment()
//        val thirdFragment= ThirdFragment()
//        val fourthFragment= FourthFragment()
//
//        setCurrentFragment(firstFragment)
//
//        bottomNavigationView = findViewById(R.id.bottomNavigationView)
//
//        bottomNavigationView.setOnNavigationItemSelectedListener {
//            when(it.itemId){
//                R.id.home->setCurrentFragment(firstFragment)
//                R.id.person->setCurrentFragment(secondFragment)
//                R.id.settings->setCurrentFragment(thirdFragment)
//                R.id.P->setCurrentFragment(fourthFragment)
//
//            }
//            true
//        }

        initUI()

    }


    private fun initUI() {
        initNavigation()
    }

    private fun initNavigation() {
        val navHost = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHost.navController
        binding.bottomNavigationView.setupWithNavController(navController)
    }





























//    private fun setCurrentFragment(fragment:Fragment)=
//        supportFragmentManager.beginTransaction().apply {
//            replace(R.id.flFragment,fragment)
//            commit()
//        }

}

