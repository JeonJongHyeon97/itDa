package com.example.itda.gamefolder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.itda.R
import com.example.itda.databinding.GameMainBinding

class GameMain : AppCompatActivity(), GameFragment.Communicator {

    var myScore = 0
    private lateinit var drawerLayout: DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        @Suppress("UNUSED_VARIABLE")
        val binding = DataBindingUtil.setContentView<GameMainBinding>(this, R.layout.game_main)

        drawerLayout = binding.drawerLayout
        //deliver score to fragment via bundle
        val gameFrag:GameFragment = GameFragment()
        supportFragmentManager.beginTransaction().replace(R.id.myNavHostFragment, gameFrag).commit()

    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return NavigationUI.navigateUp(navController, drawerLayout)
    }

    override fun passData(score:String){
        val bundle = Bundle()
        bundle.putString("inputScore", score)
        val transaction = this.supportFragmentManager.beginTransaction()
        val gameWon = GameWonFragment()
        gameWon.arguments = bundle
        transaction.replace(R.id.myNavHostFragment, gameWon)
        transaction.addToBackStack(null)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.commit()
    }

}
