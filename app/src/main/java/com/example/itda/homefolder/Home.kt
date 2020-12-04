package com.example.itda.homefolder

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.itda.AlarmPage
import com.example.itda.MyApplication
import com.example.itda.R
import com.example.itda.boardfolder.BoardDTO
import com.example.itda.boardfolder.HotpostPage
import com.example.itda.neologismfolder.NeologismData
import com.example.itda.neologismfolder.NeologismRecycleAdapter
import com.example.itda.neologismfolder.Neologism_page
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_home.*
import java.text.SimpleDateFormat
import java.util.*


class home : Fragment() {
    var firestore : FirebaseFirestore?=null
    var Useremail = MyApplication.prefs.getString("email", "aaaaaa@naver.com")
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        var nowTime =
            SimpleDateFormat("yyyyMMddHHmmss").format(Date(System.currentTimeMillis()))
        auth= Firebase.auth
        firestore = FirebaseFirestore.getInstance()
        neologismSetting()
        scheduleSetting()

        var sampleDate = "2020-06-14 10:12:14"
        var sf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var date = sf.parse(sampleDate)
        var today = Calendar.getInstance()
        var calcuDate = (today.time.time - date.time) / (60 * 60 * 24 * 1000)


        firestore?.collection("totalBoard")?.orderBy("replies",Query.Direction.DESCENDING)?.limit(4)?.get()?.addOnSuccessListener { result ->
            Log.d("firebase", "진입은 성공")
            var title_list = mutableListOf<String?>()
            var board_list = mutableListOf<String?>()
            var definition_list = mutableListOf<String?>()
            for (document in result) {
                Log.d("asdf", "${document.id} => ${document.data}")
                var oneData = document.toObject(BoardDTO::class.java)
                println(oneData)
                Log.d("firebase", "for문 돌아가는중")
                board_list.add(oneData.boardName)
                title_list.add(oneData.title)
            }
            today_text1?.text=title_list[0]
            today_text2?.text=title_list[1]
            today_text3?.text=title_list[2]
            today_text4?.text=title_list[3]
            today_title1?.text="Ask a "+board_list[0]
            today_title2?.text="Ask a "+board_list[1]
            today_title3?.text="Ask a "+board_list[2]
            today_title4?.text="Ask a "+board_list[3]

            Log.d("firebase", "for문 끝")
        }

        return view

    }
    fun neologismSetting(){
        firestore?.collection("neologism")?.orderBy("date",Query.Direction.DESCENDING)?.limit(4)?.get()?.addOnSuccessListener { result ->
            Log.d("firebase", "진입은 성공")
            var name_list = mutableListOf<String?>()
            var definition_list = mutableListOf<String?>()
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
    }
    fun scheduleSetting(){
        val adapter = FamilyScheduleRecycleAdapter()
        var dat:MutableList<String?> = mutableListOf()
        var list : Map<String,Any> = mutableMapOf("contents" to "None")
        var downloadKey :String? = "null"
        firestore!!.collection("accounts").whereEqualTo("email", Useremail).get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    var data = document.toObject(UserDTO::class.java)
                    downloadKey = data.family
                    Log.d("check", "downloadKey 세팅 완료 : $downloadKey")
                }
                firestore?.collection("calendar")?.document(downloadKey!!)?.get()
                    ?.addOnSuccessListener { document ->
                        Log.d("check", "dada size : ${document.data?.size}")
                        if (document.data?.size == null) {
                            firestore!!.collection("calendar").document(downloadKey!!)
                                .set(mutableMapOf("None" to list))
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Log.d("check", "$downloadKey")
                                        Log.d("check", "첫글 작성")
                                    }
                                }
                        } else {
                            list = document.data as Map<String, Any>
                            Log.d("schedule", "keys : ${list.keys}}")
                            Log.d("schedule", "values : ${list.values}")
                            for (map in list.values){
                                Log.d("schedule", "map : ${map}}")
                                if (map.toString().length > 15) {
                                    var sampleDate =
                                        map.toString().substring(10, map.toString().indexOf("&"))
                                    var sf = SimpleDateFormat("yyyyMMdd")
                                    var date = sf.parse(sampleDate)
                                    var today = Calendar.getInstance()
                                    var calcuDate = (today.time.time - date.time) / (60 * 60 * 24 * 1000)
                                    Log.d("schedule", "date : ${calcuDate}}")
                                    var scheduleTime =map.toString().substring(10, map.toString().indexOf("&")).toLong()
                                    var nowTime =SimpleDateFormat("yyyyMMddHH").format(Date(System.currentTimeMillis())).toLong()
                                    if (calcuDate > -30) {
                                        Log.d("schedule", "string : ${map.toString()}}")
                                        dat.add(map.toString())
                                    }
                                }
                            }
                            Log.d("firebase", "for문 끝")
                            dat.sortBy { it?.substring(10, it.toString().indexOf("&"))?.toLong() }
                            adapter.listData = dat
                            schedule_table?.adapter = adapter
                            schedule_table?.layoutManager = LinearLayoutManager(getContext())
                            }
                        }


            }
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setting_button.setOnClickListener {
            activity?.let {
                val intent = Intent(context, SettingPage::class.java)
                startActivity(intent)
            }
        }
        new_neologism_summary.setOnClickListener {
            activity?.let {
                val intent = Intent(context, Neologism_page::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
                activity?.finish()
            }
        }
        today_summary.setOnClickListener {
            activity?.let {
                val intent = Intent(context, HotpostPage::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
            }
        }
        alert.setOnClickListener {
            activity?.let {
                val intent = Intent(context, AlarmPage::class.java)
                startActivity(intent)
            }
        }
    }
    override fun onPause() {
        super.onPause()
        activity?.overridePendingTransition(0,0)
    }

}