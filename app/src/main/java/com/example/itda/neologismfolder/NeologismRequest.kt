package com.example.itda.neologismfolder

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.itda.MyApplication
import com.example.itda.boardfolder.RequestDTO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.request_neologism_page.*
import java.text.SimpleDateFormat
import java.util.*

class NeologismRequest: AppCompatActivity() {
    var firestore : FirebaseFirestore?=null
    private lateinit var auth: FirebaseAuth
    var requestDTO = RequestDTO()
    var Useremail = MyApplication.prefs.getString("email", "no email")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth= Firebase.auth
        firestore = FirebaseFirestore.getInstance()

        write_cancel.setOnClickListener{
            finish()
        }
        //class RequestDTO(var date:Long?=null, var email:String?=null, var text:String?=null, var title:String?=null)

        write_complete.setOnClickListener{
            if(write_title.text.isNullOrEmpty()){
                Toast.makeText(this, "Please enter the Title", Toast.LENGTH_SHORT).show()
            }else if(write_text.text.isNullOrEmpty()){
                Toast.makeText(this, "Please enter the Content", Toast.LENGTH_SHORT).show()
            }else {
                var time = SimpleDateFormat("yyyyMMddHHmmss").format(Date(System.currentTimeMillis())).toLong()
                requestDTO = RequestDTO(
                    time,
                    Useremail,
                    write_text.text.toString(),
                    write_title.text.toString()
                )
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
}