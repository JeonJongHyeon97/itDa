package com.example.itda

import android.content.Intent
import android.os.Bundle
import android.service.autofill.CharSequenceTransformation
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.search_neologism.*


class SearchNeologism : AppCompatActivity() {
    var firestore : FirebaseFirestore?=null
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_neologism)

        auth= Firebase.auth
        firestore = FirebaseFirestore.getInstance()

        search_button.setOnClickListener {
            var data: MutableList<NeologismData>
            val adapter = NeologismRecycleAdapter()
            var dat:MutableList<NeologismData> = mutableListOf()
            var keyword: String? = neologism_search.text.toString()
            if (keyword.isNullOrEmpty()) {
                Toast.makeText(this, "Enter at least 2 words", Toast.LENGTH_SHORT).show()
            }else {
                firestore?.collection("neologism")?.get()?.addOnSuccessListener { result ->
                    Log.d("firebase", "진입은 성공")
                    for (document in result) {
                        Log.d("asdf", "${document.id} => ${document.data}")
                        var oneData = document.toObject(NeologismData::class.java)
                        println(oneData)
                        Log.d("firebase", "for문 돌아가는중")
                        if (oneData.name!!.contains(keyword.toString())) dat.add(oneData)
                    }
                    Log.d("firebase", "for문 끝")
                    data = dat
                    if (data.isNullOrEmpty()) {
                        Toast.makeText(this, "Result is empty", Toast.LENGTH_SHORT).show()
                    } else {
                        adapter?.listData = data
                        neologism_recycle?.adapter = adapter
                        neologism_recycle?.layoutManager = LinearLayoutManager(this)
                    }
                }
            }
        }
    }

}
