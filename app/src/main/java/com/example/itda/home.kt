package com.example.itda

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_neologism.*


class home : Fragment() {
    var firestore : FirebaseFirestore?=null
    var name_list = mutableListOf<String?>()
    var definition_list = mutableListOf<String?>()
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home,container,false)
        auth= Firebase.auth
        firestore = FirebaseFirestore.getInstance()
        firestore?.collection("neologism")?.orderBy("date",
            Query.Direction.DESCENDING)?.get()?.addOnSuccessListener { result ->
            Log.d("firebase", "진입은 성공")
            for (document in result) {
                Log.d("asdf", "${document.id} => ${document.data}")
                var oneData = document.toObject(NeologismData::class.java)
                println(oneData)
                Log.d("firebase", "for문 돌아가는중")
                name_list.add(oneData.name)
                definition_list.add(oneData.definition)
            }
            neologism_title1?.text=name_list[0]
            neologism_title2?.text=name_list[1]
            neologism_title3?.text=name_list[2]
            neologism_title4?.text=name_list[3]

            neologism_text1?.text=definition_list[0]
            neologism_text2?.text=definition_list[1]
            neologism_text3?.text=definition_list[2]
            neologism_text4?.text=definition_list[3]
            Log.d("firebase", "for문 끝")
        }
        return view
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        new_neologism_summary.setOnClickListener {

        }
        today_summary.setOnClickListener {

        }

    }
}