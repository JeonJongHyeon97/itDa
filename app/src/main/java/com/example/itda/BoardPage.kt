package com.example.itda

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.board_main_page.*
import kotlinx.android.synthetic.main.fragment_board.*
import kotlinx.android.synthetic.main.fragment_neologism.*
import kotlinx.android.synthetic.main.login_page.*

class BoardPage : AppCompatActivity() {
    var firestore : FirebaseFirestore?=null
    private lateinit var auth: FirebaseAuth
    var start = 0
    var lastVisible:DocumentSnapshot? = null
    var firstVisible : DocumentSnapshot? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.board_main_page)
        var boardName = intent.getStringExtra("BoardPage")!!.toString()
        auth = Firebase.auth
        firestore = FirebaseFirestore.getInstance()
        firestore?.collection(boardName)?.orderBy("date")?.limit(10)?.get()?.addOnSuccessListener { result ->
            var size=result.size()
            Log.d("firebase", "result size : $size")
            lastVisible = result.documents[result.size()-1]
            firstVisible = result.documents[result.size()-11]}

        board_name.text="Ask a "+boardName
        loadData(boardName,firstVisible)
        board_write.setOnClickListener{
            val intent = Intent(this,WritePage::class.java)
            intent.putExtra("BoardPage", boardName)
            startActivity(intent)
            finish()
        }
        board_right_arrow.setOnClickListener{
            Log.d("firebase", "right진입은 성공")
            try{
                Log.d("firebase", "try진입은 성공")
                loadData(boardName,firstVisible)
            }catch(e:NullPointerException){
                Log.d("firebase", "exception진입은 성공")
                Toast.makeText(this, "Last page!", Toast.LENGTH_SHORT).show()
            }
        }
        board_left_arrow.setOnClickListener{
            Log.d("firebase", "left진입은 성공")
            if(start>=0){
                Log.d("firebase", "if진입은 성공")
                start-=10
                loadData(boardName,firstVisible)
            }else{
                Log.d("firebase", "else진입은 성공")
                Toast.makeText(this, "First page!", Toast.LENGTH_SHORT).show()
            }
        }

    }
    fun loadData(boardName:String, firstVisi: DocumentSnapshot?){
        var data: MutableList<BoardDTO>
        val adapter = BoardRecycleAdapter()
        var dat: MutableList<BoardDTO> = mutableListOf()
        auth = Firebase.auth
        firestore = FirebaseFirestore.getInstance()
        firestore?.collection(boardName)?.orderBy("date")?.limit(10)?.startAfter(firstVisi)?.get()?.addOnSuccessListener { result ->
                var size=result.size()
                Log.d("firebase", "result size : $size")
                //var pointer = result.documents[result.size() - 1]
                Log.d("firebase", "진입은 성공")
                for (document in result) {
                    Log.d("asdf", "${document.id} => ${document.data}")
                    var oneData = document.toObject(BoardDTO::class.java)
                    println(oneData)
                    Log.d("firebase", "for문 돌아가는중")
                    dat.add(oneData)
                }
                Log.d("firebase", "for문 끝")
                data = dat.asReversed()
                adapter.listData = data
                board_recycle.adapter = adapter
                board_recycle.layoutManager = LinearLayoutManager(this)
                //lastVisible=pointer
            }
    }
}



