package com.example.itda.gamefolder

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.itda.MyApplication
import com.example.itda.R
import com.example.itda.boardfolder.BoardDTO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.rank_page.*

class Ranking : AppCompatActivity() {
    var firestore: FirebaseFirestore? = null
    var useremail = MyApplication.prefs.getString("email", "no email")
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.rank_page)

        auth = Firebase.auth
        firestore = FirebaseFirestore.getInstance()
        //order by score, load only 10 highest data
        firestore?.collection("score")?.orderBy("score",Query.Direction.DESCENDING)?.
        limit(10)?.get()?.addOnSuccessListener { result ->
            Log.d("firebase", "진입은 성공")
            var title_list = mutableListOf<String?>() //Email
            var board_list = mutableListOf<String?>() //Score
            for (document in result) {
                Log.d("asdf", "${document.id} => ${document.data}")
                var oneData = document.toObject(ScoreDTO::class.java)
                println(oneData)
                Log.d("firebase", "for문 돌아가는중")
                title_list.add(oneData.userEmail)
                board_list.add(oneData.score.toString())
            }
            today_title1?.text=title_list[0]
            today_title2?.text=title_list[1]
            today_title3?.text=title_list[2]
            today_title4?.text=title_list[3]
            today_title5?.text=title_list[4]
            today_title6?.text=title_list[5]
            today_title7?.text=title_list[6]
            today_title8?.text=title_list[7]
            today_title9?.text=title_list[8]
            today_title10?.text=title_list[9]
            today_text1?.text="Score "+board_list[0]
            today_text2?.text="Score "+board_list[1]
            today_text3?.text="Score "+board_list[2]
            today_text4?.text="Score "+board_list[3]
            today_text5?.text="Score "+board_list[4]
            today_text6?.text="Score "+board_list[5]
            today_text7?.text="Score "+board_list[6]
            today_text8?.text="Score "+board_list[7]
            today_text9?.text="Score "+board_list[8]
            today_text10?.text="Score "+board_list[9]

            Log.d("firebase", "for문 끝")
        }
        exit_button.setOnClickListener {
            finish()
        }


    }
}