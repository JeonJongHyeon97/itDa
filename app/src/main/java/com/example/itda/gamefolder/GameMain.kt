package com.example.itda.gamefolder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.itda.R
import com.example.itda.databinding.GameMainBinding

class GameMain : AppCompatActivity() {
    var myScore = 0
    private lateinit var drawerLayout: DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("UNUSED_VARIABLE")
        val binding = DataBindingUtil.setContentView<GameMainBinding>(this, R.layout.game_main)

        drawerLayout = binding.drawerLayout
        //deliver score to fragment via bundle
        val gameFrag:GameFragment = GameFragment()
        var bundle = Bundle()
        bundle.putInt("key",myScore)
        gameFrag.arguments = bundle
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.myNavHostFragment, gameFrag)
        transaction.commit()

    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return NavigationUI.navigateUp(navController, drawerLayout)
    }

}
