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
import kotlinx.android.synthetic.main.write_post_page.*
import java.text.SimpleDateFormat
import java.util.*

class RequestNewBoard: AppCompatActivity() {
    var firestore : FirebaseFirestore?=null
    private lateinit var auth: FirebaseAuth
    var boardDTO = BoardDTO()
    var Useremail = MyApplication.prefs.getString("email", "aaaaaa@naver.com")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.request_new_board)
        auth= Firebase.auth
        firestore = FirebaseFirestore.getInstance()

        write_cancel.setOnClickListener{
            finish()
        }
        //data class BoardDTO(var boardName:String?=null, var date:Long?=null, var email:String?=null,
        //                   var like:Long?=null, var reply:Map<String,String>?=null,
        //                   var text:String?=null, var title:String?=null)

        write_complete.setOnClickListener{
            if(write_title.text.isNullOrEmpty()){
                Toast.makeText(this, "Please enter the Title", Toast.LENGTH_SHORT).show()
            }else if(write_text.text.isNullOrEmpty()){
                Toast.makeText(this, "Please enter the Content", Toast.LENGTH_SHORT).show()
            }else {
                var time =
                    SimpleDateFormat("yyyyMMddHHmmss").format(Date(System.currentTimeMillis()))
                        .toLong()
                boardDTO = BoardDTO("Request New Board",
                    time,
                    Useremail,
                    0,
                    mutableListOf(),
                    write_text.text.toString(),
                    write_title.text.toString()
                )
                firestore!!.collection("boardRequest").document(time.toString()).set(boardDTO)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Write post complete!!", Toast.LENGTH_SHORT).show()
                        }
                    }
                finish()
            }
        }
    }
}