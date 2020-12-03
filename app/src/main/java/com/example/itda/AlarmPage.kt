package com.example.itda

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.itda.neologismfolder.NeologismData
import com.example.itda.neologismfolder.NeologismRecycleAdapter
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.alarm_page)
        auth = Firebase.auth
        firestore = FirebaseFirestore.getInstance()

        var data: MutableList<NeologismData>
        val adapter = AlarmRecyclerViewAdapter()
        var dat:MutableList<AlarmDTO> = mutableListOf()

        var alarmSnapshot: ListenerRegistration? = null
        val alarmDTOList = ArrayList<AlarmDTO>()


        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        println(uid)
        FirebaseFirestore.getInstance()
            .collection("alarms")
            .whereEqualTo("destinationUid", uid)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                alarmDTOList.clear()
                if (querySnapshot == null) return@addSnapshotListener
                for (snapshot in querySnapshot?.documents!!) {
                    alarmDTOList.add(snapshot.toObject(AlarmDTO::class.java)!!)
                }
                alarmDTOList.sortByDescending { it.timestamp }

                adapter?.listData = data
                neologism_recycle?.adapter = adapter
                neologism_recycle?.layoutManager = LinearLayoutManager(this)
            }


    }

}