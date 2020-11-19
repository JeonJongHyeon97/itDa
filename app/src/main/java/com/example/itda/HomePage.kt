package com.example.itda

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*

class HomePage : AppCompatActivity() {
    private val fragment_board by lazy {board()}
    private val fragment_calendar by lazy {calendar()}
    private val fragment_home by lazy {home()}
    private val fragment_neologism by lazy {neologism()}
    private val fragment_setting by lazy {setting()}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initNavigationBar()
    }

    private fun initNavigationBar() {
        nav_view.run {
            setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.board -> {
                        changeFragment(fragment_board)
                    }
                    R.id.calendar -> {
                        changeFragment(fragment_calendar)
                    }
                    R.id.home -> {
                        changeFragment(fragment_home)
                    }
                    R.id.neologism -> {
                        changeFragment(fragment_neologism)
                    }
                    R.id.setting -> {
                        changeFragment(fragment_setting)
                    }
                }
                true
            }
            selectedItemId = R.id.board
        }
    }
    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .commit()
    }
}




