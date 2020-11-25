package com.example.itda

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_neologism.*


class neologism : Fragment() {
    var firestore : FirebaseFirestore?=null
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_neologism,container,false)
        var data: MutableList<NeologismData>
        val adapter = NeologismRecycleAdapter()
        var dat:MutableList<NeologismData> = mutableListOf()
        auth= Firebase.auth
        firestore = FirebaseFirestore.getInstance()
        firestore?.collection("neologism")?.get()?.addOnSuccessListener { result ->
            Log.d("firebase", "진입은 성공")
            for (document in result) {
                Log.d("asdf", "${document.id} => ${document.data}")
                var oneData = document.toObject(NeologismData::class.java)
                println(oneData)
                Log.d("firebase", "for문 돌아가는중")
                dat.add(oneData)
            }
            Log.d("firebase", "for문 끝")
            data = dat
            adapter?.listData = data
            neologism_recycle?.adapter = adapter
            neologism_recycle?.layoutManager = LinearLayoutManager(getContext())
        }
        return view
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        neologism_request.setOnClickListener{
            val intent = Intent(getContext(),NeologismRequest::class.java)
            startActivity(intent)
        }
        search_layout.setOnClickListener {
            activity?.let {
                val intent = Intent(context, SearchNeologism::class.java)
                startActivity(intent)
            }
        }





    }

}


