package com.example.itda

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.board_main_page.*
import kotlinx.android.synthetic.main.board_main_page.board_write
import kotlinx.android.synthetic.main.signup_page.*
import kotlinx.android.synthetic.main.write_post_page.*
import java.text.SimpleDateFormat
import java.util.*

class WritePage: AppCompatActivity() {
    var firestore : FirebaseFirestore?=null
    private lateinit var auth: FirebaseAuth
    var boardDTO = BoardDTO()
    var Useremail = MyApplication.prefs.getString("email", "no email")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.write_post_page)
        auth= Firebase.auth
        firestore = FirebaseFirestore.getInstance()

        var boardName = intent.getStringExtra("BoardPage").toString()
        Log.d("BoardName","${boardName}")

        write_cancel.setOnClickListener{
            finish()
        }
        //data class BoardDTO(var date:Long?=null, var email:String?=null,
         //                   var like:Long?=null, var reply:Map<String,String>?=null,
         //                   var text:String?=null, var title:String?=null)

        write_complete.setOnClickListener{
            var time = SimpleDateFormat("yyyyMMddHHmmss").format(Date(System.currentTimeMillis())).toLong()
            boardDTO = BoardDTO(time, Useremail,0, null, write_text.text.toString(), write_title.text.toString())
            firestore!!.collection(boardName).document(time.toString()).set(boardDTO)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Write post complete!!", Toast.LENGTH_SHORT).show()
                    }
                }
            val intent = Intent(this,BoardPage::class.java)
            intent.putExtra("BoardPage", boardName)
            startActivity(intent)
            finish()
        }
    }
}