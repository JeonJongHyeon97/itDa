package com.example.itda.boardfolder

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.itda.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.board_main_page.*
import kotlinx.android.synthetic.main.board_main_page.search_layout

class BoardPage : AppCompatActivity() {
    var firestore : FirebaseFirestore?=null
    private lateinit var auth: FirebaseAuth
    var start = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.board_main_page)
        var boardName = intent.getStringExtra("BoardPage")!!.toString()

        loadData(boardName)

        board_name.text="Ask a "+boardName
        board_write.setOnClickListener{
            val intent = Intent(this, WritePage::class.java)
            intent.putExtra("BoardPage", boardName)
            startActivity(intent)
            finish()
        }
        search_layout.setOnClickListener {
            val intent = Intent(this, SearchBoard::class.java)
            intent.putExtra("BoardPage", boardName)
            startActivity(intent)
        }


    }
    fun loadData(boardName:String){
        var data: MutableList<BoardDTO>
        val adapter = BoardRecycleAdapter()
        var dat: MutableList<BoardDTO> = mutableListOf()
        auth = Firebase.auth
        firestore = FirebaseFirestore.getInstance()
        firestore?.collection(boardName)?.orderBy("date",Query.Direction.DESCENDING)?.limit(50)?.get()?.addOnSuccessListener { result ->
                var size=result.size()
                Log.d("firebase", "result size2 : $size")

                Log.d("firebase", "진입은 성공")
                for (document in result) {
                    Log.d("asdf", "${document.id} => ${document.data}")
                    var oneData = document.toObject(BoardDTO::class.java)
                    println(oneData)
                    Log.d("firebase", "for문 돌아가는중")
                    dat.add(oneData)
                }
                Log.d("firebase", "for문 끝")
                data = dat
                adapter.listData = data
                board_recycle_view.adapter = adapter
                board_recycle_view.layoutManager = LinearLayoutManager(this)
                //firstVisible=pointer
            }
    }

}



