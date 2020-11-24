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

class NeologismRequest: AppCompatActivity() {
    var firestore : FirebaseFirestore?=null
    private lateinit var auth: FirebaseAuth
    var requestDTO = RequestDTO()
    var Useremail = MyApplication.prefs.getString("email", "no email")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.request_neologism_page)
        auth= Firebase.auth
        firestore = FirebaseFirestore.getInstance()

        write_cancel.setOnClickListener{
            finish()
        }
        //class RequestDTO(var date:Long?=null, var email:String?=null, var text:String?=null, var title:String?=null)

        write_complete.setOnClickListener{
            var time = SimpleDateFormat("yyyyMMddHHmmss").format(Date(System.currentTimeMillis())).toLong()
            requestDTO = RequestDTO(time, Useremail,write_text.text.toString(), write_title.text.toString())
            firestore!!.collection("neologismRequest").document(time.toString()).set(requestDTO)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Request complete!!", Toast.LENGTH_SHORT).show()
                    }
                }
            finish()
        }
    }
}