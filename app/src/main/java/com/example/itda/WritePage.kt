package com.example.itda

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.board_main_page.*
import kotlinx.android.synthetic.main.board_main_page.board_write
import kotlinx.android.synthetic.main.write_post_page.*

class WritePage: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.write_post_page)
        var boardName = intent.getStringExtra("BoardPage").toString()
        write_cancel.setOnClickListener{
            finish()
        }
        write_complete.setOnClickListener{
            finish()
        }
    }
}