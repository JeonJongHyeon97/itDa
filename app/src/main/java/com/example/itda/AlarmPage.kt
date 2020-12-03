package com.example.itda

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.itda.neologismfolder.NeologismData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.alarm_page.*
import kotlinx.android.synthetic.main.search_neologism.*
import java.util.ArrayList


class AlarmPage : AppCompatActivity() {
    var firestore : FirebaseFirestore?=null
    private lateinit var auth: FirebaseAuth
    var alarmSnapshot: ListenerRegistration? = null
    var Useremail = MyApplication.prefs.getString("email", "aaaaaa@naver.com")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.alarm_page)
        auth = Firebase.auth
        firestore = FirebaseFirestore.getInstance()

//        var data: MutableList<NeologismData>
        val adapter = AlarmRecyclerViewAdapter()
        var dat:MutableList<AlarmDTO> = mutableListOf()
//
//        val alarmDTOList = ArrayList<AlarmDTO>()
        Log.d("alarm","$Useremail")
//        FirebaseFirestore.getInstance()
//            .collection("alarm")
//            .whereEqualTo("destinationUid", Useremail)
//            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
//                dat.clear()
//                if (querySnapshot == null) return@addSnapshotListener
//                for (snapshot in querySnapshot?.documents!!) {
//                    dat.add(snapshot.toObject(AlarmDTO::class.java)!!)
//                }
//                dat.sortByDescending { it.replyDate }
//                adapter?.listData = dat
//                alarm_recycle_view?.adapter = adapter
//                alarm_recycle_view?.layoutManager = LinearLayoutManager(this)
//            }
        firestore?.collection("alarm")?.whereEqualTo("destinationEmail", Useremail)?.limit(10)?.get()?.addOnSuccessListener { result ->
            for (document in result) {
                Log.d("asdf", "${document.id} => ${document.data}")
                var oneData = document.toObject(AlarmDTO::class.java)
                println(oneData)
                Log.d("firebase", "for문 돌아가는중")
                dat.add(oneData)
            }
            if (dat.isNullOrEmpty()) {
                Toast.makeText(this, "No alarm !", Toast.LENGTH_SHORT).show()
            } else {
                dat.sortByDescending { it.replyDate }
                adapter?.listData = dat
                alarm_recycle_view?.adapter = adapter
                alarm_recycle_view?.layoutManager = LinearLayoutManager(this)
            }
        }


    }

}