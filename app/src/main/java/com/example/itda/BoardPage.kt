package com.example.itda

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.board_main_page.*
import kotlinx.android.synthetic.main.login_page.*

class BoardPage : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.board_main_page)
        var boardName = intent.getStringExtra("BoardPage")!!.toString()
        board_write.setOnClickListener{
            val intent = Intent(this,WritePage::class.java)
            startActivity(intent)
            intent.putExtra("BoardPage", boardName)
        }
    }
}



