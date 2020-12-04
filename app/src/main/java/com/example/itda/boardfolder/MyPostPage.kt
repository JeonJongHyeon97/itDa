package com.example.itda.boardfolder

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.itda.MyApplication
import com.example.itda.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.my_post_page.*


class MyPostPage : AppCompatActivity() {
    var firestore : FirebaseFirestore?=null
    var useremail = MyApplication.prefs.getString("email", "no email")
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_post_page)

        loadData()

        board_name.text="My Post"

    }
    fun loadData(){ //load the post which is written by user
        var data: MutableList<BoardDTO>
        val adapter = MypostRecycleAdapter()
        var dat: MutableList<BoardDTO> = mutableListOf()
        auth = Firebase.auth
        firestore = FirebaseFirestore.getInstance()
        Log.d("Mypost", "$useremail")
        firestore?.collection("totalBoard")?.orderBy("date",Query.Direction.DESCENDING)?.
        limit(50)?.get()?.addOnSuccessListener { result ->
            for (document in result) {
                Log.d("asdf", "${document.id} => ${document.data}")
                var oneData = document.toObject(BoardDTO::class.java)
                println(oneData)
                if ((oneData.email==useremail)) dat.add(oneData)
            }
            data = dat
            //show each post through recycler view
            adapter.listData = data
            board_recycle_view.adapter = adapter
            board_recycle_view.layoutManager = LinearLayoutManager(this)
        }
    }
}



