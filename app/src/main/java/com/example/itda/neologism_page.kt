package com.example.itda


import androidx.appcompat.app.AppCompatActivity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_neologism.*
import kotlinx.android.synthetic.main.login_page.*
import kotlinx.android.synthetic.main.neologism_page.*
import kotlinx.android.synthetic.main.neologism_page.nav_view
import kotlinx.android.synthetic.main.neologism_page.neologism_recycle
import kotlinx.android.synthetic.main.neologism_page.neologism_request
import kotlinx.android.synthetic.main.neologism_page.search_layout
import java.security.AccessController.getContext


class neologism_page : AppCompatActivity()  {
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
            selectedItemId = R.id.neologism
        }
    }
    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .commit()
    }
}
