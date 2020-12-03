package com.example.itda.homefolder

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.itda.R
import com.example.itda.board
import com.example.itda.calendarfolder.calendar
import com.example.itda.familydiaryfolder.FamilyboardFragment
import com.example.itda.neologismfolder.neologism
import kotlinx.android.synthetic.main.activity_main.*

class HomePage : AppCompatActivity() {
    private val fragment_board by lazy { board() }
    private val fragment_calendar by lazy { calendar() }
    private val fragment_home by lazy { home() }
    private val fragment_neologism by lazy { neologism() }
    private val fragment_familyboard by lazy { FamilyboardFragment() }
    var time3: Long = 0

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
                    R.id.family_board -> {
                        changeFragment(fragment_familyboard)
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
//                    R.id.setting -> {
//                        changeFragment(fragment_setting)
//                    }
                }
                true
            }
            selectedItemId = R.id.home
        }
    }
    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .commit()
    }
    override fun onBackPressed() {
        val time1 = System.currentTimeMillis()
        val time2 = time1 - time3
        if (time2 in 0..2000) {
            finish()
        }
        else {
            time3 = time1
            Toast.makeText(applicationContext, "한번 더 누르면 종료됩니다", Toast.LENGTH_SHORT).show()
        }
    }
}




