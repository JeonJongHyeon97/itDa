package com.example.itda.boardfolder

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.itda.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.my_post_page.*
import java.text.SimpleDateFormat
import java.util.*


class HotpostPage : AppCompatActivity() {
    var firestore : FirebaseFirestore?=null
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_post_page)

        loadData()

        board_name.text="Hot post"

    }
    fun loadData(){
        var nowTime = SimpleDateFormat("yyyyMMddHHmmss").format(Date(System.currentTimeMillis())).toLong()
        var data: MutableList<BoardDTO>
        val adapter = HotpostRecycleAdapter()
        var dat: MutableList<BoardDTO> = mutableListOf()
        auth = Firebase.auth
        firestore = FirebaseFirestore.getInstance()
        firestore?.collection("totalBoard")?.orderBy("replies",Query.Direction.DESCENDING)?.limit(50)?.get()?.addOnSuccessListener { result ->
            var size=result.size()
            Log.d("firebase", "result size2 : $size")

            Log.d("firebase", "진입은 성공")
            for (document in result) {
                Log.d("asdf", "${document.id} => ${document.data}")
                var oneData = document.toObject(BoardDTO::class.java)
                println(oneData)
                Log.d("firebase", "for문 돌아가는중")
                if((!oneData.replies!!.isNullOrEmpty())&&((nowTime - oneData.date!!)>2))
                dat.add(oneData)
            }
            Log.d("firebase", "for문 끝")
            data = dat
            adapter.listData = data
            board_recycle_view.adapter = adapter
            board_recycle_view.layoutManager = LinearLayoutManager(this)
        }
    }
}



