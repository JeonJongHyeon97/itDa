package com.example.itda

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.content_page.*
import kotlinx.android.synthetic.main.fragment_neologism.*
import java.security.AccessController.getContext

class BoardDetail : AppCompatActivity() {
    var firestore: FirebaseFirestore? = null
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_page)

        val boardName = intent.getStringExtra("BoardPage")!!.toString()
        val title = intent.getStringExtra("Title")!!.toString()
        val date = intent.getStringExtra("Date")!!.toString()
        val email = intent.getStringExtra("Email")!!.toString()
        val like = intent.getStringExtra("Like")!!.toString()
        val reply = intent.getStringArrayListExtra("Reply")?.toMutableList()
        val text = intent.getStringExtra("Text")!!.toString()

        var data: MutableList<String?>
        val adapter = ReplyRecycleAdapter()
        var dat:MutableList<String?> = mutableListOf()

        Log.d("QQ","Reply : $reply")

        post_date.text = date.substring(0,4)+"."+date.substring(4,6)+"."+date.substring(6,8)+" "+date.substring(8,10)+":"+date.substring(10,12)
        post_title.text = title
        post_text.text = text
        post_like.text = like + " Likes"
        if (!reply.isNullOrEmpty()) {
            for (document in reply!!) {
                dat.add(document)
            }
            Log.d("firebase", "for문 끝")
            data = dat
            adapter?.listData = data
            post_recycle_view?.adapter = adapter
            post_recycle_view?.layoutManager = LinearLayoutManager(this)
        }


    }
}